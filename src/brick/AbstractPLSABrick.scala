package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import documents.Document
import attribute.AttributeType
import matrix.{AttributedPhi, Theta}
import utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 20:31
 */
/**
 * brick execute E\--step for given parameter, than it apply regularizer and M\--step for matrix phi
 * @param regularizer regularizer to apply
 * @param phiSparsifier sparsifier for phi matrix
 * @param attribute attribute of brick (process only phi matrix with corresponding attribute)
 * @param modelParameters number of topics and number of words
 */
abstract class AbstractPLSABrick(private val regularizer: Regularizer,
                                 private val phiSparsifier: Sparsifier,
                                 private val attribute: AttributeType,
                                 private val modelParameters: ModelParameters) {

    /**
     * execute one iteration
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @param documents seq of documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double

    /**
     * apply regularizer
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     */
    protected def applyRegularizer(theta: Theta, phi: AttributedPhi) {
        require(phi.attribute == attribute, "matrix and brick attribute does not correspond")
        var w = 0
        var t = 0
        while (t < phi.numberOfRows) {
            while (w < phi.numberOfColumns) {
                phi.addToExpectation(t, w, regularizer.derivativeByPhi(attribute)(t: Int, w: Int, theta: Theta, phi: AttributedPhi))
                w += 1
            }
            w = 0
            t += 1
        }
    }

    /**
     * calculate probability to generate word from topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @param theta matrix of distribution of documents by topics
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
