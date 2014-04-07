package initialapproximationgenerator

import utils.ModelParameters
import documents.Document
import matrix.{Ogre, AttributedPhi, Theta}
import attribute.AttributeType
import java.util.Random

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 15:27
 */
class RandomInitialApproximationGenerator(private val random: Random) extends InitialApproximationGenerator {


    protected def fullMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]){
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
