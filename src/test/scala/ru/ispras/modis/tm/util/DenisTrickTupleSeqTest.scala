package ru.ispras.modis.tm.util

import org.scalatest.{FunSuite}
import ru.ispras.modis.tm.utils.{DenisTrickTupleSeq, TupleArraySeq}

import scala.util.Random

/**
 * Created by valerij on 12/11/14.
 */
class DenisTrickTupleSeqTest extends FunSuite {
    private val random = new Random()

    test("test denis' structure") {
        test(5)
        test(10)
        test(15)
        test(30)
        test(100)
    }

    private def test(length : Int): Unit = {
        for (_ <- 0 until 10000)
            test(generateInt2Short(length))
    }

    private def generateInt2Short(length : Int) = seq2Pairs((0 until length).flatMap(_ => {
        val value = random.nextInt(1000)
        (0 until random.nextInt(5)).map(_ => value)
    }))

    private def test(int2short : Seq[(Int,Short)]) = {
        val tupleArray = DenisTrickTupleSeq(int2short.toArray)

        assert(tupleArray.toVector.toMap == int2short.toMap)
    }

    private def seq2Pairs(seq : Seq[Int]) : Seq[(Int,Short)] = {
        seq.groupBy(x => x).toSeq.map{case(w, ww) => (w, ww.length.toShort)}
    }
}
