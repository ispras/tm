package plsa

import attribute.AttributeType
import brick.AbstractPLSABrick
import stoppingcriteria.StoppingCriteria
import sparsifier.Sparsifier
import matrix.{AttributedPhi, Theta}
import documents.Document
import regularizer.Regularizer
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:37
 */
/**
 * main part of all project. It class put all bricks together and process training algorithm
 * @param bricks plsaBrick to process one attribute
 * @param stoppingCriteria check is it time to stop
 * @param thetaSparsifier sparsify matrix theta
 * @param regularizer regularizer, for example direchlet
 * @param phi words by topic distribution matrix
 * @param theta document by topic distribution matrix
 */
class PLSA(private val bricks: Map[AttributeType, AbstractPLSABrick],
           private val stoppingCriteria: StoppingCriteria,
           private val thetaSparsifier: Sparsifier,
           private val regularizer: Regularizer,
           private val phi: Map[AttributeType, AttributedPhi],
           private val theta: Theta) extends Logging {

    /**
     * take sequence of documents into input and train model on it
     * @param documents sequence of documents (after numerator)
     * @return trained model (matrix phi and theta)
     */
    def train(documents: Seq[Document]): TrainedModel = {
        val collectionLength = documents.foldLeft(0) {
            (sum, document) => sum + document.numberOfWords()
        }
        var numberOfIteration = 0
        var oldPpx = 0d
        var newPpx = 0d
        while (!stoppingCriteria(numberOfIteration, oldPpx, newPpx)) {
            oldPpx = newPpx
            newPpx = makeIteration(numberOfIteration: Int, collectionLength: Int, documents: Seq[Document])
            numberOfIteration += 1
        }

        new TrainedModel(phi, theta)
    }

    /**
     * calculate perplexity by given loglikelihood and length  of collection
     * @param logLikelihood logarithm of likelihood to observe collection: ln(p(D\ Phi, Theta))
     * @param collectionLength number of words in collection
     * @return perplexity of model
     */
    protected def perplexity(logLikelihood: Double, collectionLength: Int) = math.exp(-logLikelihood / collectionLength)

    /**
     * performe iteration
     * @param iterationCnt serial number of iteration
     * @param collectionLength number of words in collection
     * @param documents sequence of input documents
     * @return perplexity of model after iteration
     */
    protected def makeIteration(iterationCnt: Int, collectionLength: Int, documents: Seq[Document]) = {
        val logLikelihood = bricks.foldLeft(0d) {case (sum, (attribute, brick)) =>
                sum + brick.makeIteration(theta, phi(attribute), documents, iterationCnt)
        }
        val newPpx = perplexity(logLikelihood, collectionLength)
        info(iterationCnt + " " + newPpx)
        applyRegularizer()
        theta.dump()
        theta.sparsify(thetaSparsifier, iterationCnt)
        newPpx
    }

    /**
     * apply regularizer to matrix theta
     */
    private def applyRegularizer() {
        regularizer.regularizeTheta(phi, theta)
    }
}
