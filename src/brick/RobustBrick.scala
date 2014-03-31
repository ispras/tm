package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import attribute.AttributeType
import utils.ModelParameters
import matrix.{AttributedPhi, Theta}
import documents.Document
import scala.collection.mutable

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
                          private val wordsFromBackground: Array[Float],
                          private val background: Array[Float],
                          private val noise: Array[mutable.Map[Int, Float]]) extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters) {

    if (noiseParameters.backgroundWeight + noiseParameters.noiseWeight == 0) println("you better use NonRobustPLSABrick")

    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Float = {

        var logLikelihood = 0f
        var documentNumber = 0
        while (documentNumber < documents.size) {
            logLikelihood += processOneDocument(documents(documentNumber), documentNumber, theta, phi)
            documentNumber += 1
        }
        updateBackground()
        phi.dump()
        logLikelihood
    }

    private def processOneDocument(document: Document,
                                   documentNumber: Int,
                                   theta: Theta,
                                   phi: AttributedPhi): Float = {
        var logLikelihood = 0f
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, documentNumber, theta, phi, noise(documentNumber))
        }
        updateNoise(noise(documentNumber))
        logLikelihood
    }

    private def processOneWord(wordIndex: Int,
                               numberOfWords: Int,
                               documentNumber: Int,
                               theta: Theta,
                               phi: AttributedPhi,
                               noise: mutable.Map[Int, Float]): Float = {
        val z = (modelParameters.topics.foldLeft(0f) {
            (sum, topic) => sum + phi.probability(topic, wordIndex) * theta.probability(documentNumber, topic)
        } + noiseParameters.backgroundWeight * background(wordIndex) + noiseParameters.noiseWeight * noise(wordIndex)) / (1 + noiseParameters.noiseWeight + noiseParameters.backgroundWeight)

        require(z > 0, z + " " + noise)
        var topic = 0
        while (topic < modelParameters.numberOfTopics) {
            val ndwt = numberOfWords * theta.probability(documentNumber, topic) * phi.probability(topic, wordIndex) / z
            theta.addToExpectation(documentNumber, topic, ndwt)
            phi.addToExpectation(topic, wordIndex, ndwt)
            topic += 1
        }

        noise(wordIndex) = numberOfWords * noise(wordIndex) * noiseParameters.noiseWeight / z
        wordsFromBackground(wordIndex) += numberOfWords * background(wordIndex) * noiseParameters.backgroundWeight / z
        numberOfWords * math.log(z).toFloat
    }

    private def updateNoise(noise: mutable.Map[Int, Float]) {
        val sum = noise.values.sum
        require(sum > 0 || noiseParameters.noiseWeight == 0, noise)
        noise.keys.toArray.foreach {
            key => noise(key) = if (noiseParameters.noiseWeight != 0) noise(key) / sum else 1f / noise.size
        }
    }


    private def updateBackground() {
        var i = 0
        val sum = wordsFromBackground.sum
        require(sum > 0 || noiseParameters.backgroundWeight == 0)
        while (i < background.size) {
            background(i) = if (noiseParameters.backgroundWeight > 0) wordsFromBackground(i) / sum else 1f / background.length
            wordsFromBackground(i) = 0
            i += 1
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
        val wordsFromBackground = new Array[Float](modelParameters.numberOfWords(attribute))
        val background = 0.until(modelParameters.numberOfWords(attribute)).map(i => 1f / modelParameters.numberOfWords(attribute)).toArray
        val noise = documents.map {
            doc => val size = doc.getAttributes(attribute).size
                doc.getAttributes(attribute).foldLeft(mutable.Map[Int, Float]()) {
                    case (map, (wordIndex, numberOfWords)) => map + (wordIndex -> 1f / size)
                }
        }.toArray

        new RobustBrick(regularizer, phiSparsifier, attribute, modelParameters, noiseParameters, wordsFromBackground, background, noise)
    }
}
