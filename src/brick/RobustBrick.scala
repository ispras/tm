package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import attribute.AttributeType
import utils.ModelParameters
import matrix.{Background, AttributedPhi, Theta}
import documents.Document
import scala.collection.mutable
import grizzled.slf4j.Logging


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 15:46
 */
class RobustBrick private(regularizer: Regularizer,
                          phiSparsifier: Sparsifier,
                          attribute: AttributeType,
                          modelParameters: ModelParameters,
                          private val noiseParameters: NoiseParameters,
                          private val background: Background,
                          private val noise: Array[mutable.Map[Int, Float]])
    extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters) with Logging {

    require(background.attribute == attribute)
    if (noiseParameters.backgroundWeight + noiseParameters.noiseWeight == 0)
        warn("noise and background weight are equal to zero. You'd better use NonRobustPLSABrick")

    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double = {

        var logLikelihood = 0f
        for (document <- documents) {
            logLikelihood += processOneDocument(document, theta, phi)
        }
        if (noiseParameters.backgroundWeight > 0) background.dump()
        phi.dump()
        logLikelihood
    }

    private def processOneDocument(document: Document,
                                   theta: Theta,
                                   phi: AttributedPhi): Float = {
        var logLikelihood = 0f
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, document.serialNumber, theta, phi, noise(document.serialNumber))
        }
        updateNoise(noise(document.serialNumber))
        logLikelihood
    }

    private def processOneWord(wordIndex: Int,
                               numberOfWords: Int,
                               documentIndex: Int,
                               theta: Theta,
                               phi: AttributedPhi,
                               noise: mutable.Map[Int, Float]): Float = {
        val Z = (countZ(phi, theta, wordIndex, documentIndex) + noiseParameters.backgroundWeight * background.probability(wordIndex)
            + noiseParameters.noiseWeight * noise(wordIndex)) / (1 + noiseParameters.noiseWeight + noiseParameters.backgroundWeight)

        require(Z > 0, Z + " " + noise)
        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val ndwt = numberOfWords * theta.probability(documentIndex, topic) * phi.probability(topic, wordIndex) / Z
            theta.addToExpectation(documentIndex, topic, ndwt)
            phi.addToExpectation(topic, wordIndex, ndwt)
            topic += 1
        }

        noise(wordIndex) = numberOfWords * noise(wordIndex) * noiseParameters.noiseWeight / Z
        background.addToExpectation(wordIndex, numberOfWords * background.probability(wordIndex) * noiseParameters.backgroundWeight / Z)
        numberOfWords * math.log(Z).toFloat
    }

    private def updateNoise(noise: mutable.Map[Int, Float]) {
        val sum = noise.values.sum
        require(sum > 0 || noiseParameters.noiseWeight == 0, noise)
        for (key <- noise.keys) {
            noise(key) = if (noiseParameters.noiseWeight != 0) noise(key) / sum else 1f / noise.size
        }
    }

}

object RobustBrick {
    def apply(regularizer: Regularizer,
              phiSparsifier: Sparsifier,
              attribute: AttributeType,
              modelParameters: ModelParameters,
              noiseParameters: NoiseParameters,
              documents: Seq[Document]) = {

        val background = Background(attribute, modelParameters)
        val noise = documents.map {
            doc => val size = doc.getAttributes(attribute).size
                doc.getAttributes(attribute).foldLeft(mutable.Map[Int, Float]()) {
                    case (map, (wordIndex, numberOfWords)) => map + (wordIndex -> 1f / size)
                }
        }.toArray

        new RobustBrick(regularizer, phiSparsifier, attribute, modelParameters, noiseParameters, background, noise)
    }
}
