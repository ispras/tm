package ru.ispras.modis.tm.utils

/**
 * Created by valerij on 7/15/14.
 */
trait FloatMatrixTraverser {
    def forforfor(matrix: Array[Array[Float]])(rowColOp: (Int, Int) => Unit) {
        forfor(matrix) { x => 0} { (x, y, z) => rowColOp(x, y)}
    }

    def forfor(matrix: Array[Array[Float]])(rowOp: Int => Float)(rowColOp: (Int, Int, Float) => Unit) = {
        val numberOfRows = matrix.length
        val numberOfColumns = matrix.head.length

        var columnIndex = 0
        var rowIndex = 0

        while (rowIndex < numberOfRows) {
            val intermediate = rowOp(rowIndex)
            while (columnIndex < numberOfColumns) {
                rowColOp(rowIndex, columnIndex, intermediate)
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }
}
