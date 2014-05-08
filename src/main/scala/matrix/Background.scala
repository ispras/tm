package matrix

import utils.ModelParameters
import scala.Array
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 01.04.14
 * Time: 12:42
 */
/**
 * hold distribution of words by background and expectation of words, produced by background
 * @param attribute type of main.scala.attribute
 * @param ogre anonymous ogre, hold expectation and stochastic main.scala.matrix with one row
 */
class Background private(val attribute: AttributeType, private val ogre: Ogre) {
    /**
     * probability to generate word from background
     * @param wordIndex serial number of word
     * @return p(wordIndex l background)
     */
    def probability(wordIndex: Int): Float = ogre.probability(0, wordIndex)

    /**
     * add value to corresponding element of expectation main.scala.matrix
     * @param wordIndex word serial number
     * @param value value to add
     */
    def addToExpectation(wordIndex: Int, value: Float): Unit = ogre.addToExpectation(0, wordIndex, value)

    /**
     * return expected number of words wordIndex, produced by background
     * @param wordIndex serial number of word
     * @return expected number of words
     */
    def expectation(wordIndex: Int): Float = ogre.expectation(0, wordIndex)

    /**
     * dump values from expectation main.scala.matrix to stochastic main.scala.matrix,
     * perform normalisation of stochastic main.scala.matrix,
     * replace all values in expectation main.scala.matrix by zeros
     */
    def dump() = ogre.dump()

    /**
     * number of words is equal to number of columns in main.scala.matrix
     * @return number of columns in main.scala.matrix
     */
    def numberOfWords = ogre.numberOfColumns
}

/**
 * companion object that help to construct background with given parameters.
 * Perform uniform initialisation of background e.g. every word has the same weight
 */
object Background {
    /**
     * construct background
     * @param attribute main.scala.attribute type
     * @param modelParameters model parameters hold information about number of words.
     * @return instance of class background
     */
    def apply(attribute: AttributeType, modelParameters: ModelParameters) = {
        val expectationMatrix = Array(Array.fill[Float](modelParameters.numberOfWords(attribute))(1f))

        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)

        val background = new Background(attribute, new Ogre(expectationMatrix, stochasticMatrix){})
        background.dump()
        background
    }
}
