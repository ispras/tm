package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.tm.attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:25
 */
class ZeroRegularizer extends Regularizer {
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) {}

    def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) {}
}
