package ru.ispras.modis.tm.buildertest

import org.scalatest.{Matchers, FlatSpec}
import ru.ispras.modis.tm.builder.LDABuilder
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import org.mockito.Mockito
import java.util.Random


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.05.14
 * Time: 17:00
 */
class LDABuilderTest extends FlatSpec with Matchers {
    val docs = Mockito.mock(classOf[List[Document]])
    val alphabet = Mockito.mock(classOf[Alphabet])
    val random = new Random()
    it should "throw exception when alpha <= -1" in {
        a [IllegalArgumentException] should be thrownBy  {
            new LDABuilder(100, alphabet, docs, -1, 0f, random)
        }

        a [IllegalArgumentException] should be thrownBy  {
            new LDABuilder(100, alphabet, docs, -1.1f, 0f, random)
        }
    }

    it should "throw exception when beta <= -1" in {
        a [IllegalArgumentException] should be thrownBy  {
            new LDABuilder(100, alphabet, docs, 1, -1f, random)
        }

        a [IllegalArgumentException] should be thrownBy  {
            new LDABuilder(100, alphabet, docs, 1, -2f, random)
        }
    }
}
