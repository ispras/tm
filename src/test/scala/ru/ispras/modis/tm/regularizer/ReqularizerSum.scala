package ru.ispras.modis.tm.regularizer


import ru.ispras.modis.tm.regularizer.Regularizer.toRegularizerSum
import org.mockito.Mockito
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 23.06.14
 * Time: 19:57
 */
class ReqularizerSum extends FlatSpec with Matchers  {
    val r1 = Mockito.mock(classOf[Regularizer])
    val r2 = Mockito.mock(classOf[Regularizer])
    val r3 = Mockito.mock(classOf[Regularizer])

    "sum of regularizers " should "be regularizer" in {
        (r1 + r2 + r3).isInstanceOf[Regularizer]  should be (true)
    }
}
