package brick.fixedphi

import utils.ModelParameters
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import brick.AbstractPLSABrick
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 05.05.14
 * Time: 16:24
 */
abstract class BrickPhiFixed(attribute: AttributeType,
                              modelParameters: ModelParameters)
    extends AbstractPLSABrick(new ZeroRegularizer, new ZeroSparsifier, attribute, modelParameters) {
}
