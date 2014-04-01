package plsa

import initialapproximationgenerator.InitialApproximationGenerator
import regularizer.Regularizer
import documents.{Alphabet, Document}
import utils.ModelParameters
import sparsifier.Sparsifier
import brick.NonRobustBrick
import stoppingcriteria.StoppingCriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 17:34
 */
object PLSAFactory {
    //TODO BUILDer
    def apply(initialApproximationGenerator: InitialApproximationGenerator,
              regularizer: Regularizer,
              documents: Seq[Document],
              numberOfTopics: Int,
              thetaSparsifier: Sparsifier,
              phiSparsifier: Sparsifier,
              stoppingCriteria: StoppingCriteria,
              alphabet: Alphabet) = {
        val numberOfWords = alphabet.wordsMap.map {
            case (key, value) => (key, value.size)
        }
        val modelParameters = new ModelParameters(numberOfTopics, numberOfWords)
        val bricks = alphabet.wordsMap.map {
            case (key, value) => (key, new NonRobustBrick(regularizer, phiSparsifier, key, modelParameters))
        }
        val (theta, phi) = initialApproximationGenerator.apply(modelParameters, documents)
        new PLSA(bricks, stoppingCriteria, thetaSparsifier, regularizer, phi, theta)
    }
}
