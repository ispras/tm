package regularizer

import matrix.{AttributedPhi, Theta}
import attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 15:30
 */
abstract class Regularizer(private val regulizerParameter: Float) {
    // TODO add abstraction level
    def apply(theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float

    def derivativeByTheta(d: Int, t: Int, theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float

    def derivativeByPhi(attribute: AttributeType)(t: Int, w: Int, theta: Theta, phi: AttributedPhi): Float
}

object Regularizer {
    implicit def toRegularizerSum(reqularizer: Regularizer): RegularizerSum = new RegularizerSum(Vector(reqularizer))
}