package ru.ispras.modis.tm.matrix

import org.scalatest.{Matchers, FlatSpec}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 22.04.14
 * Time: 12:19
 */
class MatrixForSparsifierTest extends FlatSpec with Matchers {
    "new main.scala.matrix " should "be unchanged" in {
        val array = Array.fill[Array[Float]](20)(Array.fill[Float](10)(0.1f))
        val matrix = new MatrixForSparsifier(array)
        matrix.isNormalised should be(true)
        matrix.numberOfColumns() should be(10)
        matrix.numberOfRows should be(20)
        matrix.setZero(3, 4)
        matrix.isNormalised should be(false)
        matrix(3, 4) should be(0.0f)
        matrix(4, 4) should be(0.1f)
        array(3)(4) should be(0.0f)
        array(5)(2) should be(0.1f)
    }
}
