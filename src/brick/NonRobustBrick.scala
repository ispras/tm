package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import attribute.AttributeType
import matrix.{AttributedPhi, Theta}
import documents.Document
import utils.ModelParameters
import math.log

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 16:44
 */
class NonRobustBrick(regularizer: Regularizer, phiSparsifier: Sparsifier, attribute: AttributeType, modelParameters: ModelParameters) extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters) {
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Float = {
        var documentNumber = 0
        var logLikelihood = 0f

        documents.foreach {
            doc => logLikelihood += processOneDocument(doc, documentNumber, theta, phi); documentNumber += 1
        }
        applyRegularizer(theta, phi)
        phi.dump()
        logLikelihood
    }

    private def processOneDocument(document: Document, documentNumber: Int, theta: Theta, phi: AttributedPhi) = {
        if (document.attributes.contains(attribute)) {
            var logLikelihood = 0f
            var wordsNum = 0
            while (wordsNum < document.getAttributes(attribute).length) {
                val (wordIndex, numberOfWords) = document.getAttributes(attribute)(wordsNum)
                logLikelihood += processOneWord(wordIndex, numberOfWords, documentNumber, phi, theta)
                wordsNum += 1
            }
            logLikelihood
        }
        else 0f
    }

    protected def processOneWord(wordIndex: Int, numberOfWords: Int, documentNumber: Int, phi: AttributedPhi, theta: Theta): Float = {
        val z = modelParameters.topics.foldLeft(0f)((sum, topic) => sum + theta.probability(documentNumber, topic) * phi.probability(topic, wordIndex))
        var likelihood = 0f
        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val ndwt = numberOfWords * theta.probability(documentNumber, topic) * phi.probability(topic, wordIndex) / z
            theta.addToExpectation(documentNumber, topic, ndwt)
            phi.addToExpectation(topic, wordIndex, ndwt)
            likelihood += phi.probability(topic, wordIndex) * theta.probability(documentNumber, topic)
            topic += 1
        }
        numberOfWords * log(likelihood).toFloat
    }
}
