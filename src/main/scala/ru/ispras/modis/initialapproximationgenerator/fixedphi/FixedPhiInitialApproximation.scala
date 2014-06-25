package ru.ispras.modis.initialapproximationgenerator.fixedphi

import ru.ispras.modis.matrix.{Theta, AttributedPhi}
import ru.ispras.modis.utils.ModelParameters
import ru.ispras.modis.documents.Document
import ru.ispras.modis.initialapproximationgenerator.InitialApproximationGenerator
import ru.ispras.modis.attribute.AttributeType

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
    def apply(parameters: ModelParameters, documents: Seq[Document]): (Theta, Map[AttributeType, AttributedPhi]) = {
        val theta =  Theta(createMatrix( documents.length, parameters.numberOfTopics))
        fullMatrixTheta(parameters, documents, theta)
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
    protected def fullMatrixTheta(parameters: ModelParameters, documents: Seq[Document], theta: Theta): Unit
}
