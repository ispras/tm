package matrix

import org.scalatest.{Matchers, FlatSpec}
import attribute.Category

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 18.04.14
 * Time: 20:05
 */
class OgreTest extends FlatSpec with Matchers  {

    it should "throw exception " in {
        val expectationMatrix = Array.fill[Array[Float]](10)(new Array[Float](20))
        val stochasticMatrix = Array.fill[Array[Float]](20)(new Array[Float](10))
        a [IllegalArgumentException] should be thrownBy  {
            new Ogre(expectationMatrix, stochasticMatrix){}
        }
    }

    "size of matrices " should " be appropriate " in {
        val expectationMatrix = Array.fill[Array[Float]](10)(new Array[Float](20))
        val ogre = new Ogre(expectationMatrix, expectationMatrix){}
        ogre.numberOfColumns.shouldEqual(20)
        ogre.numberOfRows.shouldEqual(10)
    }

    "dump " should "work correct" in {
        val expectationMatrix = Array.fill[Array[Float]](2)(new Array[Float](2))
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val ogre = new Ogre(expectationMatrix, stochasticMatrix){}
        ogre.addToExpectation(0, 0, 1)
        ogre.addToExpectation(0, 1, 1)
        ogre.addToExpectation(1, 0, 1)
        ogre.addToExpectation(1, 1, 1)
        ogre.expectation(0, 0).shouldEqual(1)
        ogre.dump()
        ogre.expectation(0, 0).shouldEqual(0)
        ogre.probability(0, 0).shouldEqual(0.5f)
    }

    it should "throw exception " in {
        val expectationMatrix = Array.fill[Array[Float]](2)(new Array[Float](2))
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val ogre = new Ogre(expectationMatrix, stochasticMatrix){}
        a [IllegalArgumentException] should be thrownBy  {
            ogre.dump()
        }
    }

    it should "throw exception when NaN is added" in {
        val expectationMatrix = Array.fill[Array[Float]](2)(new Array[Float](2))
        val stochasticMatrix = Ogre.stochasticMatrix(expectationMatrix)
        val ogre = new Ogre(expectationMatrix, stochasticMatrix){}
        ogre.addToExpectation(0, 0, 1)
        ogre.addToExpectation(0, 1, 1)
        ogre.addToExpectation(1, 0, 1)
        ogre.addToExpectation(1, 1, Float.NaN)
        ogre.expectation(0, 0).shouldEqual(1)
        a [IllegalArgumentException] should be thrownBy  {
            ogre.dump()
        }
    }

}
