package matrix

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 14:11
 */
class AttributedPhi private(expectation: Array[Array[Float]], stochasticMatrix: Array[Array[Float]], val attribute: AttributeType) extends Ogre(expectation, stochasticMatrix) {
    override def probability(numberOfTopic: Int, wordIndex: Int): Float = super.probability(numberOfTopic, wordIndex)
}

object AttributedPhi {
    def apply(expectationMatrix: Array[Array[Float]], attribute: AttributeType) = {
        val stochasticMatrix = Ogre.stockasticMatrix(expectationMatrix)
        val phi = new AttributedPhi(expectationMatrix, stochasticMatrix, attribute)
        phi.dump()
        phi
    }
}

