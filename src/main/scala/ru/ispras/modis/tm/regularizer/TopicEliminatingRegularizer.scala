package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, ImmutablePhi, ImmutableTheta, Theta}

/**
 * Created by valerij on 7/9/14.
 *
 * This regularizer is described in Vorontsov, Potapenko (2014) paper.
 *
 * The idea is to decrease values theta_td for topics s.t. p(t) is low
 */

/**
 *
 * @param documents documents you are going to perform training on
 * @param regularizationParameter the higher this parameter is, the stronger topic elemination will be.
 *                                If it's excessively high, you can possibly get theta_{td}=0 forall t.
 */
class TopicEliminatingRegularizer(private val documents: Seq[Document],
                                  private val regularizationParameter: Float) extends Regularizer {
    require(regularizationParameter >= 0, "regularizationParameter  should be >= 0")

    private val docLengths = documents.map(doc => doc.attributeSet().map(attr => doc.getAttributes(attr).map(_._2).sum).sum).toArray

    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = {
        val nt = calculateNt(theta)

        -regularizationParameter * (0 until theta.numberOfTopics).foldLeft(0d)((sum, topic) => sum + math.log(
            (0 until theta.numberOfDocuments).foldLeft(0f)((sum, doc) => nt(topic) / docLengths(doc) * theta.probability(doc, topic))
        )).toFloat
    }


    override private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit = {
        val nt = calculateNt(theta)

        theta.addToExpectation { (docInd, topic) =>
            if (nt(topic) > 0) -regularizationParameter * docLengths(docInd).toFloat / nt(topic) * theta.probability(docInd, topic)
            else 0
        }
    }

    override private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit = {}


    private def calculateNt(theta: Theta) = {
        val topics = 0 until theta.numberOfTopics
        val documents = 0 until theta.numberOfDocuments

        val nt = topics.map(topic => documents.foldLeft(0f)((sum, doc) => sum + theta.expectation(doc, topic)))
        nt
    }
}
