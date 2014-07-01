package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 20:58
 */
class MatrixForSparsifier(private val data: Array[Array[Float]]) {
    private var unchanged = true

    def isNormalised = unchanged

    def apply(rowIndex: Int, columnIndex: Int) = data(rowIndex)(columnIndex)

    def numberOfRows() = data.length

    def numberOfColumns() = data.head.length

    def setZero(rowIndex: Int, columnIndex: Int) {
        unchanged = false
        data(rowIndex)(columnIndex) = 0f
    }
}
