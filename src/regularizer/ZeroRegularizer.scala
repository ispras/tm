package regularizer

import matrix.{AttributedPhi, Theta}
import attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:25
 */
class ZeroRegularizer extends Regularizer(1f) {
    def derivativeByPhi(attribute: AttributeType)(t: Int, w: Int, theta: Theta, phi: AttributedPhi): Float = 0f

    def derivativeByTheta(d: Int, t: Int, theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = 0f

    def apply(theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = 0f
}
