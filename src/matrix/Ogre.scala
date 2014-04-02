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


    def dump() {
        //unionDump()
        copyToStochasticMatrix()
        zeroAllTheShit()
    }


    def sparsify(sparsifier: Sparsifier) {
        val matrixForSparsifier = new MatrixForSparsifier(stochasticMatrix)
        sparsifier(matrixForSparsifier)
        if (!matrixForSparsifier.isNormalised) normalise()
    }

    override def toString = stochasticMatrix.map(_.mkString(", ")).mkString("\n")

    private def normalise() {
        var columnIndex = 0
        var rowIndex = 0
        while(rowIndex < numberOfRows) {
            val sum = stochasticMatrix(rowIndex).sum
            while(columnIndex < numberOfColumns) {
                stochasticMatrix(rowIndex)(columnIndex) /= sum
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }
    
    

    private def copyToStochasticMatrix() {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < numberOfRows) {
            while (columnIndex < numberOfColumns) {
                stochasticMatrix(rowIndex)(columnIndex) = expectationMatrix(rowIndex)(columnIndex)
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
        normalise()
    }

    private def zeroAllTheShit() {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < numberOfRows) {
            while (columnIndex < numberOfColumns) {
                expectationMatrix(rowIndex)(columnIndex) = 0f
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }
}

object Ogre {
    def stochasticMatrix(expectationMatrix: Array[Array[Float]]) = {
        Array.fill[Array[Float]](expectationMatrix.length)(new Array[Float](expectationMatrix.head.length))
    }
}


