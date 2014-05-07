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
/**
 * Generate initial approximation for distribution of words by topic (matrix phi) and distribution of
 * document by topic (matrix theta)
 */
trait PhiThetaApproximationGenerator extends InitialApproximationGenerator {
    /**
     * generate matrices phi and theta, full expectation with function fullMatrix and dump.
     * @param parameters parameter of models contain size of matrices
     * @param documents sequence of input documents
     * @return initialized matrix theta and phi
     */
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

    /**
     * this method full matrices phi and theta by some initial values.
     * !WARNING! do NOT do dump before return matrices
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     * @param phi matrix with zero values in expectation and stochastic matrix
     */
    protected def fullMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]): Unit

}
