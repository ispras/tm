package brick.fixedphi

import attribute.AttributeType
import utils.ModelParameters
import matrix.{AttributedPhi, Theta}
import documents.Document
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import brick.AbstractPLSABrick

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
