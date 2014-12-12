package ru.ispras.modis.tm.util

import org.scalatest.{Matchers, FlatSpec}
import ru.ispras.modis.tm.utils.TupleArraySeq

/**
 * Created by valerij on 12/9/14.
 */
class TupleArraySeqTest extends FlatSpec with Matchers {
    "iterator" should "iterate" in {
        val int2short = Seq((1, 2), (2, 3), (4, 5))
        val tupleArray = TupleArraySeq(int2short.toArray)

        tupleArray.toVector should be (int2short.toVector)
    }
}
