package builder

import documents.{Document, Alphabet}
import initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import java.util.Random
import utils.ModelParameters
import attribute.AttributeType
import brick.{NonRobustBrick, AbstractPLSABrick}
import sparsifier.{ZeroSparsifier, Sparsifier}
import stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import regularizer.{ZeroRegularizer, Regularizer}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 18:54
 */
class PLSABuilder(numberOfTopics: Int,
                  alphabet: Alphabet,
                  documents: Seq[Document],
                  protected val random: Random,
                  protected val numberOfIteration: Int)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents) {

    protected def initialApproximationGenerator: InitialApproximationGenerator = new RandomInitialApproximationGenerator(random)

    protected def thetaSparsifier: Sparsifier = new ZeroSparsifier()

    protected def phiSparsifier: Sparsifier = new ZeroSparsifier()

    protected def stoppingCriteria: StoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration)

    protected def regularizer: Regularizer = new ZeroRegularizer()

    protected def brickBuilder(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = modelParameters.numberOfWords.map {
        case (key, value) => (key, new NonRobustBrick(regularizer, phiSparsifier, key, modelParameters))
    }
}
