package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import matrix.{AttributedPhi, Theta}
import documents.Document
import utils.ModelParameters
import math.log
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 16:44
 */
/**
 * 
 * @param regularizer main.scala.regularizer to apply
 * @param phiSparsifier main.scala.sparsifier for phi main.scala.matrix
 * @param attribute main.scala.attribute of main.scala.brick (process only phi main.scala.matrix with corresponding main.scala.attribute)
 * @param modelParameters number of topics and number of words
 */
class NonRobustBrick(regularizer: Regularizer, phiSparsifier: Sparsifier, attribute: AttributeType, modelParameters: ModelParameters) extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters) {
    /**
     *
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @param documents seq of main.scala.documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double = {
        var logLikelihood = 0d

        for (doc <- documents if doc.contains(attribute)) {
            logLikelihood += processSingleDocument(doc, theta, phi)
        }
        applyRegularizer(theta, phi)
        phi.dump()
        phi.sparsify(phiSparsifier, iterationCnt)
        logLikelihood
    }

    /**
     * calculate n_dwt for given document and update expectation main.scala.matrix
     * @param document document to process
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @return log likelihood of observed document. log(P(d\ theta, phi))
     */
    private def processSingleDocument(document: Document, theta: Theta, phi: AttributedPhi) = {
        var logLikelihood = 0d
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, document.serialNumber, phi, theta)
        }
        logLikelihood
    }

    /**
     * calculate n_dwt for given word in given document and update expectation main.scala.matrix
     * @param wordIndex serial number of words in alphabet
     * @param numberOfWords number of words wordIndex in document
     * @param documentIndex serial number of document in collection
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @return log likelihood to observe word wordIndex in document documentIndex
     */
    protected def processOneWord(wordIndex: Int, numberOfWords: Int, documentIndex: Int, phi: AttributedPhi, theta: Theta): Double = {
        val Z = countZ(phi, theta, wordIndex, documentIndex)
        var likelihood = 0f
        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val ndwt = numberOfWords * theta.probability(documentIndex, topic) * phi.probability(topic, wordIndex) / Z
            theta.addToExpectation(documentIndex, topic, ndwt)
            phi.addToExpectation(topic, wordIndex, ndwt)
            likelihood += phi.probability(topic, wordIndex) * theta.probability(documentIndex, topic)
            topic += 1
        }
        numberOfWords * log(likelihood)
    }
}
