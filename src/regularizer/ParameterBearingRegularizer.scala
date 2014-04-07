package regularizer

import matrix.{AttributedPhi, Theta}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 17:56
 */
final class ParameterBearingRegularizer(private val parameterlessRegularizer: ParameterlessRegularizer, private val regulizerParameter: Float) extends Regularizer {
    def apply(theta: Theta, phi: Map[AttributeType, AttributedPhi]) = regulizerParameter * parameterlessRegularizer(theta, phi)

    def derivativeByTheta(d: Int, t: Int, theta: Theta, phi: Map[AttributeType, AttributedPhi]) = {
        require(d < theta.numberOfRows && t < theta.numberOfColumns)
        regulizerParameter * parameterlessRegularizer.derivativeByTheta(d, t, theta, phi)
    }

    def derivativeByPhi(attribute: AttributeType)(t: Int, w: Int, theta: Theta, phi: AttributedPhi) = {
        require(t < theta.numberOfColumns && w < phi.numberOfColumns && attribute == phi.attribute)
        regulizerParameter * parameterlessRegularizer.derivativeByPhi(attribute: AttributeType)(t, w, theta, phi)
    }

}
