package builder

import plsa.PLSA
import attribute.AttributeType
import brick.AbstractPLSABrick
import stoppingcriteria.StoppingCriteria
import sparsifier.Sparsifier
import regularizer.Regularizer
import matrix.{Theta, AttributedPhi}
import initialapproximationgenerator.InitialApproximationGenerator
import documents.{Document, Alphabet}
import utils.ModelParameters


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 17:45
 */
/**
 * this class can build PLSA!
 */
abstract class AbstractPLSABuilder(protected val numberOfTopics: Int,
                                   protected val alphabet: Alphabet,
                                   protected val documents: Seq[Document]) {

    protected def initialApproximationGenerator: InitialApproximationGenerator

    protected def brickBuilder(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick]

    protected def stoppingCriteria: StoppingCriteria

    protected def thetaSparsifier: Sparsifier

    protected def phiSparsifier: Sparsifier

    protected def regularizer: Regularizer

    def build(): PLSA = {
        val modelParameters = new ModelParameters(numberOfTopics, alphabet.numberOfWords())
        val (theta, phi) = initialApproximationGenerator.apply(modelParameters, documents)
        new PLSA(brickBuilder(modelParameters: ModelParameters),
            stoppingCriteria: StoppingCriteria,
            thetaSparsifier: Sparsifier,
            regularizer: Regularizer,
            phi: Map[AttributeType, AttributedPhi],
            theta: Theta)
    }
}
