package ru.ispras.modis.tm.brick

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.regularizer.Regularizer
import ru.ispras.modis.tm.sparsifier.Sparsifier
import ru.ispras.modis.tm.utils.ModelParameters
import scala.collection.par._
import scala.collection.par.Scheduler.Implicits.global
import scala.collection.optimizer._

import scala.math.log

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 16:44
 */
/**
 *
 * @param regularizer regularizer to apply
 * @param phiSparsifier sparsifier for phi matrix
 * @param attribute attribute of brick (process only phi matrix with corresponding attribute)
 * @param modelParameters number of topics and number of words
 */
class NonRobustBrick(regularizer: Regularizer,
                     phiSparsifier: Sparsifier,
                     attribute: AttributeType,
                     modelParameters: ModelParameters,
                     attributeWeight: Float,
                     parallel : Boolean = false)
    extends AbstractClassicalPLSABrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight, parallel) {

    /**
     *
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @param documents seq of documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Array[Document], iterationCnt: Int): Double =  {
        val logLikelihood = processCollection(theta, phi, documents)

        applyRegularizer(theta, phi)
        phi.dump()
        phi.sparsify(phiSparsifier, iterationCnt)
        logLikelihood
    }

    /**
     * calculate n_dwt for given document and update expectation matrix
     * @param document document to process
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @return log likelihood of observed document. log(P(d\ theta, phi))
     */
    override protected def processSingleDocument(document: Document, theta: Theta, phi: AttributedPhi) : Double = {
        var logLikelihood = 0d
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, document.serialNumber, phi, theta)
        }
        logLikelihood
    }

    /**
     * calculate n_dwt for given word in given document and update expectation matrix
     * @param wordIndex serial number of words in alphabet
     * @param numberOfWords number of words wordIndex in document
     * @param documentIndex serial number of document in collection
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
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
