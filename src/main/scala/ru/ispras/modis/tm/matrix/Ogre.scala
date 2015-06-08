package ru.ispras.modis.tm.matrix

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.sparsifier.Sparsifier
import scala.math.max

import scala.collection.par._
import scala.collection.par.Scheduler.Implicits.global
import scala.collection.optimizer._

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
abstract class Ogre protected(private val expectationMatrix: Array[Array[Float]], private val stochasticMatrix: Array[Array[Float]])
    extends Logging with Serializable {
    private var wasWarned = false
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

    /**
     *
     * @param addition a function that takes a pair of indexes (row and column respectively)
     *                 and returns a value that will be added to expectationMatrix(row, column) element
     */
    def addToExpectation(addition: (Int, Int) => Float) {
        forforfor{ (row, col) =>
            addToExpectation(row, col, addition(row, col))
        }
    }

    def numberOfRows = expectationMatrix.length

    def numberOfColumns = expectationMatrix.head.length


    /**
     * copy values from expectation matrix to stochastic matrix, perform normalization of stochastic matrix, replace every
     * element in expectationMatrix by zero.
     */
    def dump() {
        copyToStochasticMatrix()
        normalise()
        zeroAllTheShit()
    }

    /**
     * some of elements in matrix may be replaced by zero without drop of quality of model. This method do this replacement.
     * @param sparsifier object of class Sparsifier. Decide what to replace
     * @param numberOfIteration nuber of iteration when the method was called
     */
    def sparsify(sparsifier: Sparsifier, numberOfIteration: Int) {
        val matrixForSparsifier = new MatrixForSparsifier(stochasticMatrix)
        sparsifier(matrixForSparsifier, numberOfIteration: Int)
        if (!matrixForSparsifier.isNormalised) normalise()
    }

    override def toString = stochasticMatrix.map(_.mkString(", ")).mkString("\n")

    /**
     * perform normalization of stochastic matrix e.g. multiply every row by 1 / (sum of row)
     */
    private def normalise() {
        forfor(calculateRowSum){ (rowIndex, columnIndex, sum) =>
            if (sum >= 2 * Float.MinPositiveValue)
                stochasticMatrix(rowIndex)(columnIndex) /= sum
            else
                stochasticMatrix(rowIndex)(columnIndex) = 1f / numberOfColumns
        }
    }

    private def calculateRowSum(rowIndex: Int) = {
        val sum = stochasticMatrix(rowIndex).sum
        checkSum(rowIndex, sum)
        sum
    }


    private def checkSum(rowIndex: Int, sum: Float) {
        require(!sum.toDouble.isNaN, "NaN is somewhere in expectation in the row " + rowIndex)
        if (sum <= 2 * Float.MinPositiveValue && !wasWarned) {
            wasWarned = true
            warn("sum should be > 0. May be you dump twice in a row. " +
                "May be the number of topics you have set is too damn high. " +
                "May be regularization is too strict. " +
                "May be a document contains no words " +
                "row number=" + rowIndex)
        }
    }

    /**
     * copy values from expectation matrix to stochastic matrix and perform normalization. Does not change
     * expectationMatrix
     */
    private def copyToStochasticMatrix() {
        forforfor{(rowIndex, columnIndex) =>
            stochasticMatrix(rowIndex)(columnIndex) = max(0f, expectationMatrix(rowIndex)(columnIndex))
        }
    }

    /**
     * replace every element in expectationMatrix by zero (before the new iteration)
     */
    private def zeroAllTheShit() {
        forforfor { (i, j) =>
            expectationMatrix(i)(j) = 0f
        }
    }

    private def forfor(rowOp: Int => Float)(rowColOp: (Int, Int, Float) => Unit) = {
        val parallelRows = (0 until numberOfRows)
        for (rowIndex <- parallelRows.toPar) {
            val intermediate = rowOp(rowIndex)
            for (columnIndex <- 0 until numberOfColumns) {
                rowColOp(rowIndex, columnIndex, intermediate)
            }
        }
    }

    private def forforfor(rowColOp: (Int, Int) => Unit) {
        forfor{ x => 0} { (x, y, z) => rowColOp(x, y)}
    }
}

/**
 * companion object help to build appropriate stochastic matrix for given expectation matrix
 */
object Ogre {
    implicit def constant2Function(value: => Float) = (_: Int, _: Int) => value

    /**
     * matrix with size, corresponds to given matrix
     * @param expectationMatrix given expectation matrix
     * @return Array[Array[Float] ] fill by zeros
     */
    def stochasticMatrix(expectationMatrix: Array[Array[Float]]) =
        Array.ofDim[Float](expectationMatrix.length, expectationMatrix.head.length)
}