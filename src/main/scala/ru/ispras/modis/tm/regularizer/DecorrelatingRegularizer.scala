package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}
import ru.ispras.modis.tm.matrix.{AttributedPhi, ImmutablePhi, ImmutableTheta, Theta}

/**
 * Created by valerij on 7/9/14.
 */
class DecorrelatingRegularizer(private val regularizationParameters: Map[AttributeType, Float]) extends Regularizer {
    def this(regularizationParameter: Float) = this(Map(DefaultAttributeType.asInstanceOf[AttributeType] -> regularizationParameter).toMap)

    require(regularizationParameters.forall(_._2 >= 0), "regularization params should be >= 0")

    override def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = {
        val topics = 0 until theta.numberOfTopics

        var result = 0f

        for ((attr, phiAttr) <- phi; t <- topics; s <- topics if s != t; word <- 0 until phiAttr.numberOfWords) {
            result += regularizationParameters(attr) * phiAttr.probability(t, word) * phiAttr.probability(s, word)
        }

        -result
    }

    override private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit = {}

    override private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit = {
        for (w <- 0 until phi.numberOfWords) {
            var sumAll = 0f
            for (s <- 0 until phi.numberOfTopics) sumAll += phi.probability(s, w)

            for (t <- 0 until phi.numberOfTopics) {
                val phiwt = phi.probability(t, w)
                phi.addToExpectation(t, w, -regularizationParameters(phi.attribute) * phiwt * (sumAll - phiwt))
            }
        }
    }
}
