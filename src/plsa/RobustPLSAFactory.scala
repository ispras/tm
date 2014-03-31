package plsa

import initialapproximationgenerator.InitialApproximationGenerator
import regularizer.Regularizer
import documents.{Alphabet, Document}
import sparsifier.Sparsifier
import stoppingcriteria.StoppingCriteria
import utils.ModelParameters
import brick.{NoiseParameters, RobustBrick}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 18:45
 */
object RobustPLSAFactory {
    def apply(initialApproximationGenerator: InitialApproximationGenerator,
              regularizer: Regularizer,
              documents: Seq[Document],
              numberOfTopics: Int,
              thetaSparsifier: Sparsifier,
              phiSparsifier: Sparsifier,
              stoppingCriteria: StoppingCriteria,
              alphabet: Alphabet,
              noiseParameter: NoiseParameters) = {
        val numberOfWords = alphabet.wordsMap.map {
            case (key, value) => (key, value.size)
        }
        val modelParameters = new ModelParameters(numberOfTopics, numberOfWords)
        val bricks = alphabet.wordsMap.map {
            case (key, value) => (key, RobustBrick(regularizer, phiSparsifier, key, modelParameters, noiseParameter, documents))
        }
        val (theta, phi) = initialApproximationGenerator.apply(modelParameters, documents)
        new PLSA(bricks, stoppingCriteria, thetaSparsifier, regularizer, phi, theta)
    }
}
