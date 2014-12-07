package ru.ispras.modis.tm.initialapproximationgenerator.fixedphi

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.initialapproximationgenerator.InitialApproximationGenerator
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 06.05.14
 * Time: 17:58
 */
abstract class FixedPhiInitialApproximation(private val phi: Map[AttributeType, AttributedPhi]) extends InitialApproximationGenerator {
    /**
     * generate matrices phi and theta, full expectation with function fullMatrix and dump.
     * @param parameters parameter of models contain size of matrices
     * @param documents sequence of input documents
     * @return initialized matrix theta and phi
     */
    def apply(parameters: ModelParameters, documents: Array[Document]): (Theta, Map[AttributeType, AttributedPhi]) = {
        val theta = Theta(createMatrix(documents.length, parameters.numberOfTopics))
        fillMatrixTheta(parameters, documents, theta)
        theta.dump()
        (theta, phi)
    }

    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before matrices are returned
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     */
    protected def fillMatrixTheta(parameters: ModelParameters, documents: Array[Document], theta: Theta): Unit
}
