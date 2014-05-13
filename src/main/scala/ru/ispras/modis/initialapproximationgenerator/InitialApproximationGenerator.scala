package ru.ispras.modis.initialapproximationgenerator

import ru.ispras.modis.utils.ModelParameters
import ru.ispras.modis.documents.Document
import ru.ispras.modis.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.attribute.AttributeType

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
     * @param documents sequence of input main.scala.documents
     * @return initialized main.scala.matrix theta and phi
     */
    def apply(parameters: ModelParameters, documents: Seq[Document]): (Theta, Map[AttributeType, AttributedPhi])


    protected def createMatrix(numberOfRows: Int, numberOfColumns: Int): Array[Array[Float]] = {
        Array.fill[Array[Float]](numberOfRows)(new Array[Float](numberOfColumns))
    }
}
