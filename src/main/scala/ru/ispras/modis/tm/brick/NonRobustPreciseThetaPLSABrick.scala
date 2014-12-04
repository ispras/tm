package ru.ispras.modis.tm.brick

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.brick.fixedphi.NonRobustPhiFixedBrick
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.regularizer.Regularizer
import ru.ispras.modis.tm.sparsifier.Sparsifier
import ru.ispras.modis.tm.utils.ModelParameters
import scala.math._

/**
 * Created by valerij on 12/4/14.
 */
class NonRobustPreciseThetaPLSABrick(regularizer: Regularizer,
                                      phiSparsifier: Sparsifier,
                                      attribute: AttributeType,
                                      modelParameters: ModelParameters,
                                      attributeWeight: Float,
                                      thetaIterations : Int,
                                      parallel : Boolean = false)
    extends AbstractClassicalPLSABrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight, parallel) with Logging {

    private val phiFixed = new NonRobustPhiFixedBrick(attribute, modelParameters, attributeWeight)

    override def makeIteration(theta: Theta, phi: AttributedPhi, documents: Array[Document], iterationCnt: Int): Double = {
        val beg = System.currentTimeMillis()
        for (thi <- 0 until thetaIterations) {
            phiFixed.makeIteration(theta, phi, documents, iterationCnt)
            theta.dump()
        }

        val like = adjustPhi(theta, phi, documents, iterationCnt)

        info("iteration took " + (System.currentTimeMillis() - beg))

        like
    }

    private def adjustPhi(theta: Theta, phi: AttributedPhi, documents: Array[Document], iterationCnt: Int) = {
        val logLikelihood = processCollection(theta, phi, documents)

        applyRegularizer(theta, phi)
        phi.dump()
        phi.sparsify(phiSparsifier, iterationCnt)
        logLikelihood
    }

    protected def processSingleDocument(doc: Document, theta: Theta, phi: AttributedPhi) = {
        var likelihood = 0d

        for ((wordIndex, numberOfWords) <- doc.getAttributes(attribute) if wordIndex < modelParameters.numberOfWords(attribute))
            likelihood += processSingleWord(wordIndex, numberOfWords, doc.serialNumber, phi, theta)

        likelihood
    }

    protected def processSingleWord(wordIndex: Int, numberOfWords: Int, documentIndex: Int, phi: AttributedPhi, theta: Theta) : Double = {
        var likelihood = 0d

        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val Z = countZ(phi, theta, wordIndex, documentIndex)
            val ndwt = numberOfWords * theta.probability(documentIndex, topic) * phi.probability(topic, wordIndex) / Z
            phi.addToExpectation(topic, wordIndex, ndwt)

            likelihood += phi.probability(topic, wordIndex) * theta.probability(documentIndex, topic)

            topic += 1
        }

        numberOfWords * log(likelihood)
    }

}
