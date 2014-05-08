package brick.fixedphi

import utils.ModelParameters
import matrix.{AttributedPhi, Theta}
import documents.Document
import scala.math.log
import main.scala.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 06.05.14
 * Time: 15:06
 */
class NonRobustPhiFixedBrick(attribute: AttributeType, modelParameters: ModelParameters)
    extends BrickPhiFixed(attribute, modelParameters){
    /**
     * execute one iteration
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @param documents seq of main.scala.documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double = {
         documents.foldLeft(0d)((logLikelihood, document) => logLikelihood + processSingleDocument(theta, phi, document))
    }

    private def processSingleDocument(theta: Theta, phi: AttributedPhi, document: Document) = {
        var logLikelihood = 0d
        for((wordIndex, numberOfWords) <- document.getAttributes(attribute) if wordIndex < modelParameters.numberOfWords(attribute)) {
            logLikelihood += processSingleWord(wordIndex, numberOfWords, document.serialNumber, phi, theta)
        }
        logLikelihood
    }

    private def processSingleWord(wordIndex: Int, numberOfWords: Int, documentIndex: Int, phi: AttributedPhi, theta: Theta): Double = {
        val Z = countZ(phi, theta, wordIndex, documentIndex)
        var likelihood = 0f
        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val ndwt = numberOfWords * theta.probability(documentIndex, topic) * phi.probability(topic, wordIndex) / Z
            theta.addToExpectation(documentIndex, topic, ndwt)
            likelihood += phi.probability(topic, wordIndex) * theta.probability(documentIndex, topic)
            topic += 1
        }
        numberOfWords * log(likelihood)
    }

}
