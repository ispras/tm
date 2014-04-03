package regularizer

import matrix.{AttributedPhi, Theta}
import attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:02
 */
class RegularizerSum(private val regularizers: Seq[Regularizer]) extends Regularizer {
    def +(regularizer: Regularizer) = new RegularizerSum(regularizer +: regularizers)

    def derivativeByPhi(attribute: AttributeType)(d: Int, t: Int, theta: Theta, phi: AttributedPhi): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer.derivativeByPhi(attribute)(d, t, theta, phi)
    }

    def derivativeByTheta(w: Int, t: Int, theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer.derivativeByTheta(w, t, theta, phi)
    }

    def apply(theta: Theta, phi: Map[AttributeType, AttributedPhi]): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer(theta, phi)
    }
}
