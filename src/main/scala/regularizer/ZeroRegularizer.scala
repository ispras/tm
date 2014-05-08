package regularizer

import matrix.{ImmutableTheta, AttributedPhi, Theta}
import main.scala.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:25
 */
class ZeroRegularizer extends Regularizer {
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    def regularizePhi(phi: AttributedPhi, theta: ImmutableTheta) {}

    def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta) {}
}
