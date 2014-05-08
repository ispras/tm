package regularizer

import matrix.{ImmutableTheta, AttributedPhi, Theta}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 20:59
 */
class SymmetricDirichlet(private val alpha: Float, private val beta: Float) extends Regularizer {
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    def regularizePhi(phi: AttributedPhi, theta: ImmutableTheta){
        (0 until phi.numberOfRows).foreach(row => (0 until phi.numberOfColumns).foreach(column => phi.addToExpectation(row, column, alpha)))
    }

    def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta) = {
        (0 until theta.numberOfRows).foreach(row => (0 until theta.numberOfColumns).foreach(column => theta.addToExpectation(row, column, beta)))
    }
}

