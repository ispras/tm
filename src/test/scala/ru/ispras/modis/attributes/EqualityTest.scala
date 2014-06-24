package ru.ispras.modis.attributes

import org.scalatest.{FlatSpec, Matchers}
import ru.ispras.modis.attribute.{Word, Category}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 18.04.14
 * Time: 14:22
 */
class EqualityTest extends FlatSpec with Matchers  {
    "Two categories" should "be equal " in {
        val oneCategory = Category
        val otherCategory = Category
        oneCategory == otherCategory should be (true)
        oneCategory.hashCode() == otherCategory.hashCode() should be (true)
    }
    "The same language" should "be equal " in {
        val eng = new Word("en")
        val otherEng = new Word("en")
        eng == otherEng should be (true)
        eng.hashCode() == otherEng.hashCode() should be (true)
    }

    "The different language " should " not be equal" in {
        val en = new Word("en")
        val ru = new Word("ru")
        en == ru should be (false)
        en == Category should be (false)
        ru == Category should be (false)
    }

}
