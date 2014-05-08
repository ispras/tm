package matrix


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 18:56
 */
/**
 * contain distribution of main.scala.documents by topic. row is a document, column is a topic.
 * @param expectationMatrix hold expectation from E-step
 * @param stochasticMatrix hold probabilities, so sum of any row is equal to 1 and every element non-negative
 */
class Theta private(expectationMatrix: Array[Array[Float]], stochasticMatrix: Array[Array[Float]]) extends Ogre(expectationMatrix, stochasticMatrix) {
    /**
     *
     * @param documentNumber serial number of document
     * @param topic serial number of topic
     * @return weight of topic in document with number documentNumber
     */
    override def probability(documentNumber: Int, topic: Int): Float = super.probability(documentNumber, topic)

    /**
     * modify value n_dt where d = documentNumber, t = topic
     * @param documentNumber serial number of document
     * @param topic serial number of topic
     * @param value value to add
     */
    override def addToExpectation(documentNumber: Int, topic: Int, value: Float): Unit = super.addToExpectation(documentNumber, topic, value)
}

/**
 * construct theta by given expectation main.scala.matrix
 */
object Theta {
    /**
     * construct theta by given expectation main.scala.matrix
     * @param expectationMatrix expectation main.scala.matrix
     * @return main.scala.matrix Theta
     */
    def apply(expectationMatrix: Array[Array[Float]]) = {
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val theta = new Theta(expectationMatrix, stochasticMatrix)
        theta
    }
}
