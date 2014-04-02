package initialapproximationgenerator

import documents.Document
import matrix.{Theta, AttributedPhi}
import utils.ModelParameters
import attribute.AttributeType
import scala.Predef._

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 18:39
 */
trait InitialApproximationGenerator {
    def apply(parameters: ModelParameters, documents: Seq[Document]): (Theta, Map[AttributeType, AttributedPhi]) = {
        val theta = Theta(createMatrix( documents.length, parameters.numberOfTopics))
        val phi = parameters.numberOfWords.map {
            case (attribute, numberOfWords) => (attribute, AttributedPhi(createMatrix(parameters.numberOfTopics, numberOfWords), attribute))
        }

        fullMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi])
        theta.dump()
        phi.foreach{case(attribute, matrix) => matrix.dump()}
        (theta, phi)
    }

    protected def fullMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]): Unit

    private def createMatrix(numberOfRows: Int, numberOfColumns: Int): Array[Array[Float]] = {
        Array.fill[Array[Float]](numberOfRows)(new Array[Float](numberOfColumns))
    }
}
