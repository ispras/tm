package matrix

import attribute.AttributeType
import utils.ModelParameters
import scala.Array

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 01.04.14
 * Time: 12:42
 */
class Background private(val attribute: AttributeType, expectationMatrix: Array[Array[Float]], stochasticMatrix: Array[Array[Float]])
    extends Ogre(expectationMatrix, stochasticMatrix) {

    def probability(wordIndex: Int): Float = probability(0, wordIndex)


    def addToExpectation(wordIndex: Int, value: Float): Unit = addToExpectation(0, wordIndex, value)

    def expectation(wordIndex: Int): Float = expectation(0, wordIndex)
}

object Background {
    def apply(attribute: AttributeType, modelParameters: ModelParameters) = {
        val expectationMatrix = Array(Array.fill[Float](modelParameters.numberOfWords(attribute))(1f / modelParameters.numberOfWords(attribute)))

        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)

        val background = new Background(attribute, expectationMatrix, stochasticMatrix)
        background.dump()
        background
    }
}
