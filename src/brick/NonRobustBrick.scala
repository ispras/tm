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
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], numberOfIteration: Int): Double = {
        var logLikelihood = 0d

        for (doc <- documents if doc.contains(attribute)) {
            logLikelihood += processSingleDocument(doc, theta, phi)
        }
        applyRegularizer(theta, phi)
        phi.dump()
        phi.sparsify(phiSparsifier, numberOfIteration)
        logLikelihood
    }

    private def processSingleDocument(document: Document, theta: Theta, phi: AttributedPhi) = {
        var logLikelihood = 0d
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, document.serialNumber, phi, theta)
        }
        logLikelihood
    }

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
