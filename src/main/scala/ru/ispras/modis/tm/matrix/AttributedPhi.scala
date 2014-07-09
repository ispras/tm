package ru.ispras.modis.tm.matrix

import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 14:11
 * * hold information about distribution of words by topic
 */
/**
 * 
 * @param expectationMatrix hold expectation from E-step, number of words w, produced by topic t. n_tw
 *                          t is index of row, w is index of column
 * @param stochasticMatrix hold probabilities to generate words w from topic t.
 * @param attribute attribute type e.g. language
 */
class AttributedPhi private(expectationMatrix: Array[Array[Float]],
                            stochasticMatrix: Array[Array[Float]],
                            val attribute: AttributeType) extends Ogre(expectationMatrix, stochasticMatrix) {
    /**
     * probability to generate word with serial number wordIndex from topic topicIndex
     * @param topicIndex index of topic
     * @param wordIndex index of word
     * @return probability to generate word with serial number wordIndex from topic topicIndex
     */
    override def probability(topicIndex: Int, wordIndex: Int): Float = super.probability(topicIndex, wordIndex)

    /**
     * @param topicIndex index of topic (row)
     * @param wordIndex index of word (column)
     * @return element from interceptions of rowIndex row and columnIndex column of expectationMatrix matrix
     */
    override def expectation(topicIndex: Int, wordIndex: Int): Float = super.expectation(topicIndex, wordIndex)

    /**
     * add n_wt to (topicIndex, wordIndex) of expectation matrix
     * @param topicIndex index of topic (row)
     * @param wordIndex index of word (column)
     * @param nwt number of words w, produced by topic t
     */
    override def addToExpectation(topicIndex: Int, wordIndex: Int, nwt: Float): Unit = super.addToExpectation(topicIndex, wordIndex, nwt)

    /**
     * @return number of topics
     */
    override def numberOfRows: Int = super.numberOfRows

    /**
     * @return number of words
     */
    override def numberOfColumns: Int = super.numberOfColumns
}

object AttributedPhi {
    def apply(expectationMatrix: Array[Array[Float]], attribute: AttributeType) = {
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val phi = new AttributedPhi(expectationMatrix, stochasticMatrix, attribute)
        phi
    }
}

