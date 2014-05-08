package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import documents.Document
import matrix.{AttributedPhi, Theta}
import utils.ModelParameters
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 20:31
 */
/**
 * main.scala.brick execute E\--step for given parameter, than it apply main.scala.regularizer and M\--step for main.scala.matrix phi
 * @param regularizer main.scala.regularizer to apply
 * @param phiSparsifier main.scala.sparsifier for phi main.scala.matrix
 * @param attribute main.scala.attribute of main.scala.brick (process only phi main.scala.matrix with corresponding main.scala.attribute)
 * @param modelParameters number of topics and number of words
 */
abstract class AbstractPLSABrick(private val regularizer: Regularizer,
                                 private val phiSparsifier: Sparsifier,
                                 protected val attribute: AttributeType,
                                 protected val modelParameters: ModelParameters) {

    /**
     * execute one iteration
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @param documents seq of main.scala.documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double

    /**
     * apply main.scala.regularizer
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     */
    protected def applyRegularizer(theta: Theta, phi: AttributedPhi) {
        require(phi.attribute == attribute, "main.scala.matrix and main.scala.brick main.scala.attribute does not correspond")
        regularizer.regularizePhi(phi, theta)
    }

    /**
     * calculate probability to generate word from topics
     * @param phi distribution of words by topics. Attribute of phi main.scala.matrix should corresponds with main.scala.attribute of main.scala.brick
     * @param theta main.scala.matrix of distribution of main.scala.documents by topics
     * @param wordIndex serial number of word
     * @param documentIndex serial number of document
     * @return probability to generate word from topics
     */
    protected def countZ(phi: AttributedPhi, theta: Theta, wordIndex: Int, documentIndex: Int) = {
        var topicNumber = 0
        var sum = 0f

        while(topicNumber < modelParameters.numberOfTopics) {
            sum += phi.probability(topicNumber, wordIndex) * theta.probability(documentIndex, topicNumber)
            topicNumber += 1
        }
        sum
    }


}
