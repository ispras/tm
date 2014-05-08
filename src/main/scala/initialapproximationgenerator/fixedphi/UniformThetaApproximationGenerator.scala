package initialapproximationgenerator.fixedphi

import matrix.{Theta, AttributedPhi}
import utils.ModelParameters
import documents.Document
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 07.05.14
 * Time: 15:00
 */
class UniformThetaApproximationGenerator(phi: Map[AttributeType, AttributedPhi]) extends FixedPhiInitialApproximation(phi){
    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before return matrices
     * @param parameters model parameters
     * @param documents sequence of main.scala.documents
     * @param theta main.scala.matrix with zero values in expectation and stochastic main.scala.matrix
     */
    protected def fullMatrixTheta(parameters: ModelParameters, documents: Seq[Document], theta: Theta): Unit = {
        0.until(theta.numberOfRows).foreach(row => 0.until(theta.numberOfColumns).foreach(column => theta.addToExpectation(row, column, 1)))
    }

}
