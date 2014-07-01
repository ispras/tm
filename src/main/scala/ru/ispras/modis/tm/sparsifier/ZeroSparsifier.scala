package ru.ispras.modis.tm.sparsifier

import ru.ispras.modis.tm.matrix.MatrixForSparsifier


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:42
 */
/**
 * this class do nothing and do not change input matrix. If you do not want use sparsifier use this class.
 */
class ZeroSparsifier extends Sparsifier {
    /**
     * this method do nothing
     * @param matrix input matrix (phi or theta) with method setZero
     * @param numberOfIteration number of iteration, when sparsifier was called
     */
    def apply(matrix: MatrixForSparsifier, numberOfIteration: Int) {}
}
