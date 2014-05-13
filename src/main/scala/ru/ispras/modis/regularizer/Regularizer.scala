package ru.ispras.modis.regularizer

import ru.ispras.modis.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 15:30
 */
abstract class Regularizer {
    // TODO implement apply
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float

    def regularizePhi(phi: AttributedPhi, theta: ImmutableTheta): Unit

    def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta): Unit

    final def regularizePhi(phi: AttributedPhi, theta: Theta) {
        regularizePhi(phi, ImmutableTheta.toImmutableTheta(theta))
    }

}

object Regularizer {
    implicit def toRegularizerSum(reqularizer: Regularizer): RegularizerSum = new RegularizerSum(Vector(reqularizer))
}