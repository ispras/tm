package regularizer

import matrix.{AttributedPhi, Theta}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 20:59
 */
class SymmetricDirichlet(private val alpha: Float, private val beta: Float) extends ParameterlessRegularizer {
    def apply(theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = 0f

    def derivativeByTheta(d: Int, t: Int, theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = alpha

    def derivativeByPhi(attribute: AttributeType)(t: Int, w: Int, theta: Theta, phi: AttributedPhi): Float = beta
}
