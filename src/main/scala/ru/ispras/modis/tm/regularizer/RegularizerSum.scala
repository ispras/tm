package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.tm.attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:02
 */
class RegularizerSum(private val regularizers: Seq[Regularizer]) extends Regularizer {
    def +(regularizer: Regularizer) = new RegularizerSum(regularizer +: regularizers)


    def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) {
        regularizers.foreach(regularizer => regularizer.regularizeThetaImmutable(phi, theta))
    }

    def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) {
        regularizers.foreach(regularizer => regularizer.regularizePhiImmutable(phi, theta))
    }

    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer(phi, theta)
    }
}
