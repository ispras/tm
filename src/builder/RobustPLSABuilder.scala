package builder

import documents.{Document, Alphabet}
import java.util.Random
import utils.ModelParameters
import attribute.AttributeType
import brick.{NoiseParameters, RobustBrick, NonRobustBrick, AbstractPLSABrick}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:06
 */
class RobustPLSABuilder(numberOfTopics: Int,
                        alphabet: Alphabet,
                        documents: Seq[Document],
                        random: Random,
                        numberOfIteration: Int,
                        private val noiseWeight: Float,
                        private val backgroundWeight: Float) extends PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration){

    override protected def brickBuilder(modelParameters: ModelParameters) = {
        val noiseParameter = new NoiseParameters(noiseWeight, backgroundWeight)
        modelParameters.numberOfWords.map {
            case (key, value) => (key, RobustBrick(regularizer, phiSparsifier, key, modelParameters, noiseParameter, documents))
        }
    }
}
