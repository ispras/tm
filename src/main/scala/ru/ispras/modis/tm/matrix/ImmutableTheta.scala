package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 16:50
 */
class ImmutableTheta(theta: Theta) extends ImmutableOgre(theta) {

    /**
     *
     * @param documentIndex serial number of document (row)
     * @param topicIndex topic index (column)
     * @return weight of topic topicIndex in document documentIndex p(topicIndex |documentIndex)
     */
    override def probability(documentIndex: Int, topicIndex: Int): Float = super.probability(documentIndex, topicIndex)

    /**
     * @return number of topics
     */
    override def numberOfColumns: Int = super.numberOfColumns

    /**
     * @return number of words
     */
    override def numberOfRows: Int = super.numberOfRows
}

object ImmutableTheta {
    implicit def toImmutableTheta(theta: Theta) = new ImmutableTheta(theta)
}
