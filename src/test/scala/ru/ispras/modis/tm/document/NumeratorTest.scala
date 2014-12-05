package ru.ispras.modis.tm.document

import org.scalatest.{Matchers, FlatSpec}
import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.documents.{Numerator, TextualDocument}

/**
 * Created by padre on 04.12.14
 */
class NumeratorTest extends FlatSpec with Matchers {
    "numerator with threshold = 0 " should " numerate words " in {
        val td1 = new TextualDocument(Map(DefaultAttributeType -> "ducks ducks boobs".split(" ")))
        val td2 = new TextualDocument(Map(DefaultAttributeType ->"boobs boobs ass".split(" ")))
        val (docs, alphabet) = Numerator(Array(td1, td2).toIterator, 0)

        alphabet.contain("ducks") should be (true)
        alphabet.contain("boobs") should be (true)
        alphabet.contain("ass") should be (true)

        docs.head.getWords.toMap.getOrElse(alphabet.getIndex("boobs").get, 0) should be (1)
        docs.head.getWords.toMap.getOrElse(alphabet.getIndex("ducks").get, 0) should be (2)
        docs(1).getWords.toMap.getOrElse(alphabet.getIndex("boobs").get, 0) should be (2)
        docs(1).getWords.toMap.getOrElse(alphabet.getIndex("ass").get, 0) should be (1)
        docs(1).getWords.toMap.getOrElse(alphabet.getIndex("ducks").get, 0) should be (0)
    }

    "numerator with threshold = 1 " should " numerate nonrare words only  " in {
        val td1 = new TextualDocument(Map(DefaultAttributeType -> "ducks ducks boobs".split(" ")))
        val td2 = new TextualDocument(Map(DefaultAttributeType ->"boobs boobs ass".split(" ")))
        val (docs, alphabet) = Numerator(Array(td1, td2).toIterator, 1)

        alphabet.contain("ducks") should be (true)
        alphabet.contain("boobs") should be (true)
        alphabet.contain("ass") should be (false)

        alphabet.getIndex("ducks").get should be (1)
        alphabet.getIndex("boobs").get should be (0)

        docs.head.getWords.toMap.getOrElse(alphabet.getIndex("boobs").get, 0) should be (1)
        docs.head.getWords.toMap.getOrElse(alphabet.getIndex("ducks").get, 0) should be (2)
        docs(1).getWords.toMap.getOrElse(alphabet.getIndex("boobs").get, 0) should be (2)
        docs(1).getWords.toMap.getOrElse(alphabet.getIndex("ducks").get, 0) should be (0)
    }

}
