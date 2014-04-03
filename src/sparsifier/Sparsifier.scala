package sparsifier

import matrix.MatrixForSparsifier


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:32
 */
trait Sparsifier {
    def apply(matrix: MatrixForSparsifier, numberOfIteration: Int): Unit
}
