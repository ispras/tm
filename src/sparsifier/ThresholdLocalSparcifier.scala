package sparsifier

import matrix.MatrixForSparsifier

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 15:55
 */
/**
 *
 * @param threshold if matrix element less than this threshold it may be replaced by zero
 * @param startIteration do not do sparsification before this iteration
 * @param maxNumberOfZeroised maximum number of elements, that may be replaced by zero in one row.
 */
class ThresholdLocalSparcifier(private val threshold : Float, private val startIteration : Int, private val maxNumberOfZeroised : Int) extends AbstractLocalSpacifier{

    protected def isToBeZeroised(value: Float, numIter: Int): Boolean = (value < threshold) && (numIter > startIteration)

    def apply(matrix: MatrixForSparsifier, numIter : Int): Unit = {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < matrix.numberOfRows() && numIter > startIteration) {
            var zeroed = 0
            while(columnIndex < matrix.numberOfColumns() && zeroed < maxNumberOfZeroised){
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
