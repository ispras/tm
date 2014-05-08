package regularizer

import matrix.{ImmutableTheta, AttributedPhi, Theta}
import attribute.AttributeType


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:02
 */
class RegularizerSum(private val regularizers: Seq[Regularizer]) extends Regularizer {
    def +(regularizer: Regularizer) = new RegularizerSum(regularizer +: regularizers)


    def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta) {
        regularizers.foreach(regularizer => regularizer.regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta))
    }

    def regularizePhi(phi: AttributedPhi, theta: ImmutableTheta) {
        regularizers.foreach(regularizer => regularizer.regularizePhi(phi, theta))
    }

    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer(phi, theta)
    }
}
