package ru.ispras.modis.brick.fixedphi

import ru.ispras.modis.utils.ModelParameters
import ru.ispras.modis.regularizer.ZeroRegularizer
import ru.ispras.modis.sparsifier.ZeroSparsifier
import ru.ispras.modis.brick.AbstractPLSABrick
import ru.ispras.modis.attribute.AttributeType

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
