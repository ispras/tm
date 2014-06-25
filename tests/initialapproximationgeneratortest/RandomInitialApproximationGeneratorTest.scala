package initialapproximationgeneratortest

import org.scalatest.{Matchers, FlatSpec}
import documents.Document
import utils.ModelParameters
import java.util.Random
import initialapproximationgenerator.RandomInitialApproximationGenerator
import matrix.{Ogre, Background}
import attribute.Category

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 23.04.14
 * Time: 19:30
 */
class RandomInitialApproximationGeneratorTest extends FlatSpec with Matchers  {
    val params = new ModelParameters(25, Map(Category -> 1000))
    "sizes of matrices " should "be appropriate" in {
        val random = new Random
        val (theta, phi) = new RandomInitialApproximationGenerator(random).apply(params, new MockDocuments)
        theta.numberOfRows should be (10)
        theta.numberOfColumns should be (25)
        phi(Category).numberOfColumns should be (1000)
        phi(Category).numberOfRows should be (25)
    }



    {
        val random = new Random
        val (theta, phi) = new RandomInitialApproximationGenerator(random).apply(params, new MockDocuments)
        0.until(theta.numberOfRows).forall(row => 0.until(theta.numberOfColumns).forall(column => theta.expectation(row, column) == 0))
        0.until(theta.numberOfRows).forall(row => 0.until(theta.numberOfColumns).map(column => theta.probability(row, column)).sum == 1)
        dumpTest(phi(Category))
    }

    private def dumpTest(ogre: Ogre) {
        "expectation main.scala.matrix" should "be zeroed" in {
            0.until(ogre.numberOfRows).forall(row => 0.until(ogre.numberOfColumns).forall(column => ogre.expectation(row, column) == 0)) should be (true)
        }
        "sum of each row in stochastic main.scala.matrix" should "be close to one" in {
            0.until(ogre.numberOfRows).foreach{row =>
                math.abs(0.until(ogre.numberOfColumns).map(column => ogre.probability(row, column)).sum - 1) < 0.0001  should be (true)
            }
        }
        it should "throw exception " in {
            a [IllegalArgumentException] should be thrownBy  {
                ogre.dump()
            }
        }
    }
}

class MockDocuments extends Seq[Document] {
    def length: Int = 10

    def apply(idx: Int): Document = null

    def iterator: Iterator[Document] = this.toIterator
}
