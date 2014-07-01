package ru.ispras.modis.tm.brick.fixedphi

import ru.ispras.modis.tm.utils.ModelParameters
import ru.ispras.modis.tm.regularizer.ZeroRegularizer
import ru.ispras.modis.tm.sparsifier.ZeroSparsifier
import ru.ispras.modis.tm.brick.AbstractPLSABrick
import ru.ispras.modis.tm.attribute.AttributeType

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
