package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 16:46
 */
class ImmutablePhi(phi: AttributedPhi) extends ImmutableOgre(phi) {
    def numberOfWords = numberOfColumns

    def numberOfTopics = numberOfRows
}

object ImmutablePhi {
    implicit def toImmutablePhi(phi: AttributedPhi) = new ImmutablePhi(phi)
}
