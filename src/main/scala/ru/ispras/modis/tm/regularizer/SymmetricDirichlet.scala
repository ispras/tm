package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 20:59
 */
class SymmetricDirichlet(private val alpha: Float, private val beta: Float) extends Regularizer {
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) {
        (0 until phi.numberOfRows).foreach(row => (0 until phi.numberOfColumns).foreach(column => phi.addToExpectation(row, column, alpha)))
    }

    def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) = {
        (0 until theta.numberOfRows).foreach(row => (0 until theta.numberOfColumns).foreach(column => theta.addToExpectation(row, column, beta)))
    }
}

