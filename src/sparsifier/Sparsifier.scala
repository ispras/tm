package sparsifier

import matrix.MatrixForSparsificator


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:32
 */
trait Sparsifier {
    def apply(matrix: MatrixForSparsificator): Unit
}
