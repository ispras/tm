package ru.ispras.modis.tm.brick

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.regularizer.Regularizer
import ru.ispras.modis.tm.sparsifier.Sparsifier
import ru.ispras.modis.tm.utils.ModelParameters
import scala.collection.optimizer._

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
 * @param attributeWeight attribute weight show how important this attribute. For example title may be more
 *                        important than ordinary word from text.
 */
abstract class AbstractPLSABrick(private val regularizer: Regularizer,
                                 private val phiSparsifier: Sparsifier,
                                 protected val attribute: AttributeType,
                                 protected val modelParameters: ModelParameters,
                                 private val attributeWeight: Float) {

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
        regularizer.regularizePhi(phi, theta)
    }

    /**
     * calculate probability to generate word from topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @param theta matrix of distribution of documents by topics
     * @param wordIndex serial number of word
     * @param documentIndex serial number of document
     * @return probability to generate word from topics
     */
    protected def countZ(phi: AttributedPhi, theta: Theta, wordIndex: Int, documentIndex: Int) = optimize {
        var Z = 0f

        for (topicNumber <- 0 until modelParameters.numberOfTopics) {
            Z += phi.probability(topicNumber, wordIndex) * theta.probability(documentIndex, topicNumber)
        }
        Z / attributeWeight
    }


}
