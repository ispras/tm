package builder

import documents.{Document, Alphabet}
import initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import java.util.Random
import utils.ModelParameters
import brick.{NonRobustBrick, AbstractPLSABrick}
import sparsifier.{ZeroSparsifier, Sparsifier}
import stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import regularizer.{ZeroRegularizer, Regularizer}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 18:54
 */
class PLSABuilder(numberOfTopics: Int,
                  alphabet: Alphabet,
                  documents: Seq[Document],
                  protected val random: Random = new Random,
                  protected val numberOfIteration: Int = 100)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents) {

    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO user setters

    thetaSparsifier = new ZeroSparsifier()

    phiSparsifier = new ZeroSparsifier()

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration)

    regularizer = new ZeroRegularizer()

    protected def brickBuilder(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = modelParameters.numberOfWords.map {
        case (key, value) => (key, new NonRobustBrick(regularizer, phiSparsifier, key, modelParameters))
    }
}
