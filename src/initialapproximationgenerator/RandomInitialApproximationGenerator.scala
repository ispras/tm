package initialapproximationgenerator

import utils.ModelParameters
import documents.Document
import matrix.{AttributedPhi, Theta}
import attribute.AttributeType
import java.util.Random

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 15:27
 */
class RandomInitialApproximationGenerator(private val random: Random) extends InitialApproximationGenerator {
    def apply(parameters: ModelParameters, documents: Seq[Document]): (Theta, Map[AttributeType, AttributedPhi]) = {
        val theta = Theta(createMatrix(parameters.numberOfTopics, documents.length))
        val phi = parameters.numberOfWords.map {
            case (attribute, numberOfWords) => (attribute, AttributedPhi(createMatrix(numberOfWords, parameters.numberOfTopics), attribute))
        }
        (theta, phi)
    }

    private def createMatrix(numberOfColumns: Int, numberOfRows: Int) = 0.until(numberOfRows).map(i => 0.until(numberOfColumns).map(j => random.nextFloat()).toArray).toArray
}
