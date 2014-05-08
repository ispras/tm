package builder

import documents.{Document, Alphabet}
import java.util.Random
import utils.ModelParameters
import brick.{NoiseParameters, RobustBrick, NonRobustBrick, AbstractPLSABrick}
import initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:06
 */
class RobustPLSABuilder(numberOfTopics: Int,
                        alphabet: Alphabet,
                        documents: Seq[Document],
                        private val noiseWeight: Float,
                        private val backgroundWeight: Float,
                        private val random: Random = new Random,
                        private val numberOfIteration: Int = 100) extends AbstractPLSABuilder(numberOfTopics, alphabet, documents){

    override protected def buildBricks(modelParameters: ModelParameters) = {
        val noiseParameter = new NoiseParameters(noiseWeight, backgroundWeight)
        modelParameters.numberOfWords.map {
            case (key, value) => (key, RobustBrick(regularizer, phiSparsifier, key, modelParameters, noiseParameter, documents))
        }
    }

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration) // TODO if it is possible to avoid messing around with other class fields, AVOID it
    // use setters
    initialApproximationGenerator = new RandomInitialApproximationGenerator(random)
}
