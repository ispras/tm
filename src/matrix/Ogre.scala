package matrix

import sparsifier.Sparsifier

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 18:08
 */
abstract class Ogre protected(private val expectationMatrix: Array[Array[Float]], private val stochasticMatrix: Array[Array[Float]]) {
    require(expectationMatrix.length == stochasticMatrix.length && expectationMatrix.head.length == stochasticMatrix.head.length,
        "stochastic and expectation matrix should have the same number of rows and number of columns")

    def probability(rowIndex: Int, columnIndex: Int) = stochasticMatrix(rowIndex)(columnIndex)

    def expectation(rowIndex: Int, columnIndex: Int) = expectationMatrix(rowIndex)(columnIndex)

    def addToExpectation(rowIndex: Int, columnIndex: Int, value: Float) {
        expectationMatrix(rowIndex)(columnIndex) += value
    }

    def numberOfRows = expectationMatrix.length

    def numberOfColumns = expectationMatrix.head.length

    private def normalise() {   //todo in a different way
        var columnIndex = 0
        stochasticMatrix.foreach {
            row => val sum = row.sum
                require(sum > 0)
                while (columnIndex < row.size) {
                    row(columnIndex) /= sum
                    columnIndex += 1
                }
                columnIndex = 0
        }
    }

    def dump() {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < stochasticMatrix.length) {
            while (columnIndex < stochasticMatrix.head.length) {    // todo use getNumberOfRows
                stochasticMatrix(rowIndex)(columnIndex) = expectationMatrix(rowIndex)(columnIndex)
                expectationMatrix(rowIndex)(columnIndex) = 0f
                columnIndex += 1                                               // TODO write method "copy my shit"
            }
            columnIndex = 0
            rowIndex += 1
        }
        normalise()
    }

    def sparsify(sparsifier: Sparsifier) {
        val matrixForSparsifier = new MatrixForSparsificator(stochasticMatrix)  // TODO rename   Sparsificator
        sparsifier(matrixForSparsifier)
        if (!matrixForSparsifier.isNormalised) normalise()
    }

    override def toString = stochasticMatrix.map(_.mkString(", ")).mkString("\n")
}

object Ogre {
    /*protected ? */def stochasticMatrix(expectationMatrix: Array[Array[Float]])  = {
        // TODO fill
        (0 until expectationMatrix.length).map(i => new Array[Float](expectationMatrix.head.length)).toArray
    }
}


