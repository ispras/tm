package initialapproximationgenerator

import utils.ModelParameters
import documents.Document
import matrix.{Ogre, AttributedPhi, Theta}
import java.util.Random
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 15:27
 */
/**
 * generate random initial approximation. Full Phi and Theta by uniform distributed random value
 * @param random random number generator
 */
class RandomInitialApproximationGenerator(private val random: Random) extends PhiThetaApproximationGenerator {

    /**
     * full expectation main.scala.matrix by the random numbers from uniform distribution
     * @param parameters model parameters
     * @param documents sequence of main.scala.documents
     * @param theta main.scala.matrix with zero values in expectation and stochastic main.scala.matrix
     * @param phi main.scala.matrix with zero values in expectation and stochastic main.scala.matrix
     */
    protected def fillMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]){
        fullSingleMatrix(theta)
        phi.foreach{case(attribute, matrix) => fullSingleMatrix(matrix)}
    }

    private def fullSingleMatrix(matrix: Ogre) {
        var rowIndex = 0
        var columnIndex = 0
        while(rowIndex < matrix.numberOfRows) {
            while(columnIndex < matrix.numberOfColumns){
                matrix.addToExpectation(rowIndex, columnIndex, random.nextFloat())
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }
}
