package ru.ispras.modis.tm.sparsifier

import ru.ispras.modis.tm.matrix.MatrixForSparsifier

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 15:55
 */
/**
 * replace value in matrix by zero, but do not sparsify more than maxNumberOfZeroised and only if number of currant
 * iteration greater or equal than startIteration
 * @param threshold if matrix element less than this threshold it may be replaced by zero
 * @param startIteration do not do sparsification before this iteration
 * @param maxNumberOfZeroised maximum number of elements, that may be replaced by zero in one row.
 */
class ThresholdSparsifier(private val threshold: Float, private val startIteration: Int, private val maxNumberOfZeroised: Int) extends Sparsifier {

    /**
     * decide is given nest should be set to zero
     * @param value nest's value
     * @param numIter number of done iteration
     * @return true if nest should be sparsified false otherwise
     */
    protected def isToBeZeroised(value: Float, numIter: Int): Boolean = (value < threshold) && (numIter > startIteration)

    /**
     * sparsify given matrix. Do not sparsify more than maxNumberOfZeroised and only if number of currant iteration greater or
     * equal than startIteration
     * @param matrix input matrix (phi or theta) with method setZero
     * @param numIter serial number of currant iteration
     */
    def apply(matrix: MatrixForSparsifier, numIter: Int): Unit = {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < matrix.numberOfRows() && numIter > startIteration) {
            var zeroed = 0
            while (columnIndex < matrix.numberOfColumns() && zeroed < maxNumberOfZeroised) {
                if (isToBeZeroised(matrix(rowIndex, columnIndex), numIter)) {
                    zeroed += 1
                    matrix.setZero(rowIndex, columnIndex)
                }
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }
}
