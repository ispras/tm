package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.{Document, TextualDocument}
import ru.ispras.modis.tm.matrix.{Theta, ImmutablePhi, ImmutableTheta, AttributedPhi}

/**
 * Created by valerij on 7/9/14.
 */
class TopicEliminatingRegularizer(private val documents: Seq[Document],
                                  private val regularizationParameter: Float) extends Regularizer {
    require(regularizationParameter >= 0, "regularizationParameter  should be >= 0")

    private val docLengths = documents.map(doc => doc.attributeSet().map(attr => doc.getAttributes(attr).map(_._2).sum).sum).toArray

    override def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = {
        val nt = calculateNt(theta)

        -regularizationParameter * (0 until theta.numberOfTopics).foldLeft(0d)((sum, topic) => sum + math.log(
            (0 until theta.numberOfDocuments).foldLeft(0f)((sum, doc) => nt(topic) / docLengths(doc) * theta.probability(doc, topic))
        )).toFloat
    }


    override private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit = {
        val nt = calculateNt(theta)

        for (topic <- 0 until theta.numberOfTopics; docInd <- 0 until theta.numberOfDocuments)
            theta.addToExpectation(docInd,
                topic,
                -regularizationParameter * docLengths(docInd).toFloat / nt(topic) * theta.probability(docInd, topic))
    }

    override private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit = {}


    private def calculateNt(theta: Theta) = {
        val topics = 0 until theta.numberOfTopics
        val documents = 0 until theta.numberOfDocuments

        val nt = topics.map(topic => documents.foldLeft(0f)((sum, doc) => sum + theta.expectation(doc, topic)))
        nt
    }
}
