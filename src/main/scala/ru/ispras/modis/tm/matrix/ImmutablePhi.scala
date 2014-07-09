package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 16:46
 */
class ImmutablePhi(phi: AttributedPhi) extends ImmutableOgre(phi) {
    /**
     * return probability to generate word wordIndex from topic topicIndex
     * @param topicIndex index of topic (row)
     * @param wordIndex index of word (column)
     * @return p(wordIndex |topicIndex)
     */
    override def probability(topicIndex: Int, wordIndex: Int): Float = super.probability(topicIndex, wordIndex)

    /**
     * @return number of words
     */
    override def numberOfColumns: Int = super.numberOfColumns

    /**
     * @return number of topic
     */
    override def numberOfRows: Int = super.numberOfRows
}

object ImmutablePhi {
    implicit def toImmutablePhi(phi: AttributedPhi) = new ImmutablePhi(phi)
}
