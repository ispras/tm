package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 16:44
 */
class ImmutableOgre(private val ogre: Ogre) {
    def probability(rowIndex: Int, columnIndex: Int) = ogre.probability(rowIndex, columnIndex)

    def numberOfColumns = ogre.numberOfColumns

    def numberOfRows = ogre.numberOfRows
}
