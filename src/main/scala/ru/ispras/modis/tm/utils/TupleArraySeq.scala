package ru.ispras.modis.tm.utils

/**
 * Created by valerij on 12/9/14.
 */
private[tm] class TupleArraySeq(private val ints : Array[Int],
                     private val shorts : Array[Int]) extends Seq[(Int,Int)]{

    private class TupleArraySeqIterator extends Iterator[(Int,Int)] {
        private var index : Int = 0

        override def hasNext: Boolean = index < ints.size

        override def next(): (Int,Int) = {
            val next = (ints(index), shorts(index))
            index += 1

            next
        }
    }

    require(ints.size == shorts.size)

    override def length: Int = ints.size

    override def apply(idx: Int): (Int, Int) = (ints(idx), shorts(idx))

    override def iterator: Iterator[(Int, Int)] = new TupleArraySeqIterator
}

private[tm] object TupleArraySeq {
    def apply(pairs : Array[(Int,Int)]) = new TupleArraySeq(pairs.map(_._1), pairs.map(_._2))
}