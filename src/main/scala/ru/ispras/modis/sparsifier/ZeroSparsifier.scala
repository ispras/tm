package ru.ispras.modis.sparsifier

import ru.ispras.modis.matrix.MatrixForSparsifier


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:42
 */
/**
 * this class do nothing and do not change input main.scala.matrix. If you do not want use main.scala.sparsifier use this class.
 */
class ZeroSparsifier extends Sparsifier {
    /**
     * this method do nothing
     * @param matrix input main.scala.matrix (phi or theta) with method setZero
     * @param numberOfIteration number of iteration, when main.scala.sparsifier was called
     */
    def apply(matrix: MatrixForSparsifier, numberOfIteration: Int) {}
}
