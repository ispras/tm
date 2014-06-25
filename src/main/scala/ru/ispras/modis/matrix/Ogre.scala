package ru.ispras.modis.matrix

import ru.ispras.modis.sparsifier.Sparsifier
import math.max

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 18:08
 */
/**
 * hold two matrices, distribution of probabilities and expectation matrix. Matrices should have the same dimension
 * @param expectationMatrix hold expectation from E-step
 * @param stochasticMatrix hold probabilities, so sum of any row is equal to 1 and every element non-negative
 */
abstract class Ogre protected(private val expectationMatrix: Array[Array[Float]], private val stochasticMatrix: Array[Array[Float]]) {
    require(expectationMatrix.length == stochasticMatrix.length && expectationMatrix.head.length == stochasticMatrix.head.length,
        "stochastic and expectation matrix should have the same number of rows and number of columns")

    /**
     *
     * @param rowIndex index of row
     * @param columnIndex index of column
     * @return element from interceptions of rowIndex row and columnIndex column of stochastic matrix
     */
    def probability(rowIndex: Int, columnIndex: Int) = stochasticMatrix(rowIndex)(columnIndex)

    /**
     *
     * @param rowIndex index of row
     * @param columnIndex index of column
     * @return element from interceptions of rowIndex row and columnIndex column of expectationMatrix matrix
     */
    def expectation(rowIndex: Int, columnIndex: Int) = expectationMatrix(rowIndex)(columnIndex)

    /**
     * add value to (rowIndex, columnIndex) of expectation matrix
     * @param rowIndex index of row
     * @param columnIndex index of column
     * @param value value to add
     */
    def addToExpectation(rowIndex: Int, columnIndex: Int, value: Float) {
        expectationMatrix(rowIndex)(columnIndex) += value
    }

    def numberOfRows = expectationMatrix.length

    def numberOfColumns = expectationMatrix.head.length


    /**
     * copy values from expectation matrix to stochastic matrix, perform normalization of stochastic matrix, replace every
     * element in expectationMatrix by zero.
     */
    def dump() {
        copyToStochasticMatrix()
        zeroAllTheShit()
    }

    /**
     * some of elements in matrix may be replaced by zero without drop of quality of model. This method do this replacement.
     * @param sparsifier object of class Sparsifier. Decide what to replace
     * @param numberOfIteration nuber of iteration when the method was called
     */
    def sparsify(sparsifier: Sparsifier, numberOfIteration: Int) {
        val matrixForSparsifier = new MatrixForSparsifier(stochasticMatrix)
        sparsifier(matrixForSparsifier, numberOfIteration: Int )
        if (!matrixForSparsifier.isNormalised) normalise()
    }

    override def toString = stochasticMatrix.map(_.mkString(", ")).mkString("\n")

    /**
     * perform normalization of stochastic matrix e.g. multiply every row by 1 / (som of row)
     */
    private def normalise() {
        var columnIndex = 0
        var rowIndex = 0
        while(rowIndex < numberOfRows) {
            val sum = stochasticMatrix(rowIndex).sum
            require(sum > 0, "sum should be > 0. May be you dump twice in a row?")
            while(columnIndex < numberOfColumns) {
                stochasticMatrix(rowIndex)(columnIndex) /= sum
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
    }


    /**
     * copy values from expectation matrix to stochastic matrix and perform normalization. Does not change
     * expectationMatrix
     */
    private def copyToStochasticMatrix() {
        var columnIndex = 0
        var rowIndex = 0
        while (rowIndex < numberOfRows) {
            while (columnIndex < numberOfColumns) {
                stochasticMatrix(rowIndex)(columnIndex) = max(0f, expectationMatrix(rowIndex)(columnIndex))
                columnIndex += 1
            }
            columnIndex = 0
            rowIndex += 1
        }
        normalise()
    }

    /**
     * replace every element in expectationMatrix by zero (before the new iteration)
     */
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

/**
 * companion object help to build appropriate stochastic matrix for given expectation matrix
 */
object Ogre {
    /**
     * matrix with size, corresponds to given matrix
     * @param expectationMatrix given expectation matrix
     * @return Array[Array[Float] ] fill by zeros
     */
    def stochasticMatrix(expectationMatrix: Array[Array[Float]]) = {
        Array.fill[Array[Float]](expectationMatrix.length)(new Array[Float](expectationMatrix.head.length))
    }
}


