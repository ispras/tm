package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.matrix.{Theta, ImmutablePhi, ImmutableTheta, AttributedPhi}

/**
 * Created by valerij on 7/9/14.
 */
class DecorrelatingRegularizer(private val regularizationParameters: Map[AttributeType, Float]) extends Regularizer {
    require(regularizationParameters.forall(_._2 >= 0), "regularization params should be >= 0")

    override def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = {
        val topics = 0 until theta.numberOfTopics

        var result = 0f

        for ((attr, phiAttr) <- phi; t <- topics; s <- topics if s != t; word <- phiAttr.numberOfWords) {
            result += regularizationParameters(attr) * phiAttr.probability(t, word) * phiAttr.probability(s, word)
        }

        -result
    }

    override private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit = {}

    override private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit = {
        for (w <- 0 until phi.numberOfWords; t <- 0 until phi.numberOfTopics) {
            var sumAllButT = 0f
            for (s <- 0 until phi.numberOfTopics if s != t) sumAllButT += phi.probability(s, w)

            phi.addToExpectation(t, w, regularizationParameters(phi.attribute) * phi.probability(t, w) * sumAllButT)
        }
    }
}
