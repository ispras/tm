package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.tm.attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 15:30
 */
abstract class Regularizer {
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float

    final def regularizePhi(phi: AttributedPhi, theta: Theta): Unit = {
        regularizePhiImmutable(phi, ImmutableTheta.toImmutableTheta(theta))
    }

    final def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta): Unit = {
        regularizeThetaImmutable(phi.map { case (attr, phi) => (attr, ImmutablePhi.toImmutablePhi(phi))}, theta)
    }

    protected def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit

    protected def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit
}

object Regularizer {
    implicit def toRegularizerSum(reqularizer: Regularizer): RegularizerSum = new RegularizerSum(Vector(reqularizer))
}