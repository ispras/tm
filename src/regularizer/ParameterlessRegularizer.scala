package regularizer


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 17:58
 */
abstract class ParameterlessRegularizer extends Regularizer {
    def addParameter(parameter: Float) = new ParameterBearingRegularizer(this, parameter)
}
