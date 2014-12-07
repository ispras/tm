package ru.ispras.modis.tm.initialapproximationgenerator

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 07.05.14
 * Time: 14:51
 */
trait InitialApproximationGenerator {
    /**
     * generate matrices phi and theta, full expectation with function fullMatrix and dump.
     * @param parameters parameter of models contain size of matrices
     * @param documents sequence of input documents
     * @return initialized matrix theta and phi
     */
    def apply(parameters: ModelParameters, documents: Array[Document]): (Theta, Map[AttributeType, AttributedPhi])


    protected def createMatrix(numberOfRows: Int, numberOfColumns: Int): Array[Array[Float]] = {
        Array.fill[Array[Float]](numberOfRows)(new Array[Float](numberOfColumns))
    }
}
