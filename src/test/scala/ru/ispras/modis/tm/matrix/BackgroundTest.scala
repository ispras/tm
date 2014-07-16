package ru.ispras.modis.tm.matrix

import org.scalatest.{FlatSpec, Matchers}
import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 18.04.14
 * Time: 16:05
 */
class BackgroundTest extends FlatSpec with Matchers {
    val modelParameters = new ModelParameters(11, Map(DefaultAttributeType -> 4))

    "main parameters " should "be appropriate" in {
        val background = Background(DefaultAttributeType, modelParameters)
        background.numberOfWords should be(4)
        sumProb(background) should be(1f)
        sumExp(background) should be(0f)
        background.addToExpectation(0, 1f)
        background.addToExpectation(1, 1f)
        background.addToExpectation(2, 1f)
        background.addToExpectation(3, 1f)
        sumExp(background) should be(4f)
        background.dump()
        background.probability(1) should be(0.25f)
        sumExp(background) should be(0f)
    }

    private def sumProb(background: Background) = 0.until(background.numberOfWords).foldLeft(0f)((s, i) => s + background.probability(i))

    private def sumExp(background: Background) = 0.until(background.numberOfWords).foldLeft(0f)((s, i) => s + background.expectation(i))

}
