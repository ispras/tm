package matrix


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 18:56
 */
class Theta private(expectation: Array[Array[Float]], stochasticMatrix: Array[Array[Float]]) extends Ogre(expectation, stochasticMatrix) {
    override def probability(documentNumber: Int, topic: Int): Float = super.probability(documentNumber, topic)

    override def addToExpectation(documentNumber: Int, topic: Int, value: Float): Unit = super.addToExpectation(documentNumber, topic, value)
}

object Theta {
    def apply(expectationMatrix: Array[Array[Float]]) = {
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val theta = new Theta(expectationMatrix, stochasticMatrix)
        theta
    }
}
