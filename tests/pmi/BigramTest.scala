package pmi

import org.scalatest.{Matchers, FlatSpec}
import qualitimeasurment.Bigram

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 18.04.14
 * Time: 14:41
 */
class BigramTest extends FlatSpec with Matchers {
    "two bigrams with same words" should " be equal" in {
        val oneTwo = new Bigram(1, 2)
        val twoOne = new Bigram(2, 1)
        val oneMoreOneTwo = new Bigram(1, 2)
        oneTwo == twoOne should be (true)
        oneTwo.hashCode() == twoOne.hashCode() should be (true)
        oneTwo == oneMoreOneTwo should be (true)
        oneTwo.hashCode() == oneMoreOneTwo.hashCode() should be (true)
    }
    "two bigram with different words" should "be nonEqual" in {
        val oneTwo = new Bigram(1, 2)
        val threeFour = new Bigram(3, 4)
        val oneFour = new Bigram(1, 4)
        oneTwo == threeFour should be (false)
        oneTwo == oneFour should be (false)
        oneFour == threeFour should be (false)
        Set(1, 2) == oneTwo should be (false)
    }
}
