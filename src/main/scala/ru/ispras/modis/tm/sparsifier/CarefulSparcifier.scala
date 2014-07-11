package ru.ispras.modis.tm.sparsifier

import ru.ispras.modis.tm.matrix.MatrixForSparsifier

/**
 * Created by valerij on 7/11/14.
 */
class CarefulSparcifier(private val theshold: Float, private val startingNumberOfIteration: Int, private val minNonZero: Int) extends Sparsifier {
    override def apply(matrix: MatrixForSparsifier, numberOfIteration: Int): Unit = {
        if (numberOfIteration > startingNumberOfIteration)
            for (r <- 0 until matrix.numberOfRows()) {
                var nonZero = numberOfNonzero(matrix, r)
                for (c <- 0 until matrix.numberOfColumns()) {
                    if (matrix(r, c) < theshold && nonZero > minNonZero) {
                        matrix.setZero(r, c)
                        nonZero -= 1
                    }
                }
            }
    }

    private def numberOfNonzero(matrix: MatrixForSparsifier, row: Int) = (0 until matrix.numberOfColumns()).count(c => matrix(row, c) > 0f)

}
