package initialapproximationgenerator.fixedphi

import matrix.{Theta, AttributedPhi}
import utils.ModelParameters
import documents.Document
import initialapproximationgenerator.InitialApproximationGenerator
import attribute.AttributeType

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
     * @param documents sequence of input main.scala.documents
     * @return initialized main.scala.matrix theta and phi
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
     * @param documents sequence of main.scala.documents
     * @param theta main.scala.matrix with zero values in expectation and stochastic main.scala.matrix
     */
    protected def fullMatrixTheta(parameters: ModelParameters, documents: Seq[Document], theta: Theta): Unit
}
