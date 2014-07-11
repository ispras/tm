package ru.ispras.modis.tm.brick

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Background, Theta}
import ru.ispras.modis.tm.regularizer.Regularizer
import ru.ispras.modis.tm.sparsifier.Sparsifier
import ru.ispras.modis.tm.utils.ModelParameters

import scala.collection.mutable


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 15:46
 */
/**
 *
 * @param regularizer regularizer to apply
 * @param phiSparsifier sparsifier for phi matrix
 * @param attribute attribute of brick (process only phi matrix with corresponding attribute)
 * @param modelParameters number of topics and number of words
 * @param noiseParameters weight of noise and background
 * @param background distribution of common words
 * @param noise distribution of noise words in every document
 */
class RobustBrick private(regularizer: Regularizer,
                          phiSparsifier: Sparsifier,
                          attribute: AttributeType,
                          modelParameters: ModelParameters,
                          private val noiseParameters: NoiseParameters,
                          private val background: Background,
                          private val noise: Array[mutable.Map[Int, Float]],
                          attributeWeight: Float)
    extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight) with Logging {

    require(background.attribute == attribute)
    if (noiseParameters.backgroundWeight + noiseParameters.noiseWeight == 0)
        warn("noise and background weight are equal to zero. You'd better use NonRobustPLSABrick")

    /**
     *
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @param documents seq of documents to process
     * @param iterationCnt number of iteration
     * @return log likelihood of observed collection. log(P(D\ theta, phi))
     */
    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Double = {

        var logLikelihood = 0f
        for (document <- documents) {
            logLikelihood += processSingleDocument(document, theta, phi)
        }
        if (noiseParameters.backgroundWeight > 0) background.dump()
        phi.dump()
        phi.sparsify(phiSparsifier, iterationCnt)
        logLikelihood
    }

    /**
     * calculate n_dwt for given document and update expectation matrix
     * @param document document to process
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @return log likelihood of observed document. log(P(d\ theta, phi))
     */
    private def processSingleDocument(document: Document,
                                      theta: Theta,
                                      phi: AttributedPhi): Float = {
        var logLikelihood = 0f
        for ((wordIndex, numberOfWords) <- document.getAttributes(attribute)) {
            logLikelihood += processOneWord(wordIndex, numberOfWords, document.serialNumber, theta, phi, noise(document.serialNumber))
        }

        updateNoise(noise(document.serialNumber))
        logLikelihood
    }

    /**
     * calculate n_dwt for given word in given document and update expectation matrix
     * @param wordIndex serial number of words in alphabet
     * @param numberOfWords number of words wordIndex in document
     * @param documentIndex serial number of document in collection
     * @param theta matrix of distribution of documents by topics
     * @param phi distribution of words by topics. Attribute of phi matrix should corresponds with attribute of brick
     * @return log likelihood to observe word wordIndex in document documentIndex
     */
    private def processOneWord(wordIndex: Int,
                               numberOfWords: Int,
                               documentIndex: Int,
                               theta: Theta,
                               phi: AttributedPhi,
                               noise: mutable.Map[Int, Float]): Float = {
        val Z = (countZ(phi, theta, wordIndex, documentIndex) + noiseParameters.backgroundWeight * background.probability(wordIndex)
            + noiseParameters.noiseWeight * noise(wordIndex)) / (1 + noiseParameters.noiseWeight + noiseParameters.backgroundWeight)

        require(Z > 0, Z + " " + noise(wordIndex))
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

    /**
     * update distribution of noise words
     * @param noise distribution of noise words in document
     */
    private def updateNoise(noise: mutable.Map[Int, Float]) {
        val sum = noise.values.sum
        require(sum > 0 || noiseParameters.noiseWeight == 0, noise)
        for (key <- noise.keys) {
            noise(key) = if (noiseParameters.noiseWeight != 0) noise(key) / sum else 1f / noise.size
        }
    }

}

/**
 * companion object construct RobustBrick
 */
object RobustBrick extends Logging {
    /**
     * generate noise and background and put it all together in class RobustBrick
     * @param regularizer regularizer for phi
     * @param phiSparsifier sparsifier for phi
     * @param attribute type of attribute
     * @param modelParameters number of words and number of topics
     * @param noiseParameters weight of noise and background
     * @param documents sequence of documents
     * @return RobustBrick
     */
    def apply(regularizer: Regularizer,
              phiSparsifier: Sparsifier,
              attribute: AttributeType,
              modelParameters: ModelParameters,
              noiseParameters: NoiseParameters,
              documents: Seq[Document],
              attributeWeight: Float) = {

        val background = Background(attribute, modelParameters)
        val noise = generateNoise(documents: Seq[Document], attribute: AttributeType)
        new RobustBrick(regularizer, phiSparsifier, attribute, modelParameters, noiseParameters, background, noise, attributeWeight)
    }

    /**
     * generate initial noise for every document
     * @param documents sequence of documents
     * @param attribute type of attribute
     * @return Array of noise
     */
    private def generateNoise(documents: Seq[Document], attribute: AttributeType): Array[mutable.Map[Int, Float]] = {
        documents.map {
            doc => val size = doc.getAttributes(attribute).size
                mutable.Map(doc.getAttributes(attribute).map { case (wordIndex, numberOfWords) => wordIndex -> 1f / size}: _*)
        }.toArray
    }
}
