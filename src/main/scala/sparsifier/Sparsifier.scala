package sparsifier

import matrix.MatrixForSparsifier


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:32
 */
/**
 * according to sparse theory document may correspond to only a few topics, analogously for word by topic distribution
 * so low elements in main.scala.matrix phi and theta may be replaced by zero without drop of quality.
 */
trait Sparsifier {
    /**
     * take main.scala.matrix into input, replace some elements by zero
     * @param matrix input main.scala.matrix (phi or theta) with method setZero
     * @param numberOfIteration number of iteration, when main.scala.sparsifier was called
     */
    def apply(matrix: MatrixForSparsifier, numberOfIteration: Int): Unit
}
