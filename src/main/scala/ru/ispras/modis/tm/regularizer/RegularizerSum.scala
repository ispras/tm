package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.matrix.{AttributedPhi, ImmutablePhi, ImmutableTheta, Theta}


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 16:02
 */
class RegularizerSum(private val regularizers: Seq[Regularizer]) extends Regularizer {
    def +(regularizer: Regularizer) = new RegularizerSum(regularizer +: regularizers)


    override private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) {
        for (regularizer <- regularizers)
            regularizer.regularizeThetaImmutable(phi, theta)
    }

    override private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) {
        for (regularizer <- regularizers)
            regularizer.regularizePhiImmutable(phi, theta)
    }

    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = regularizers.foldLeft(0f) {
        (sum, regularizer) => sum + regularizer(phi, theta)
    }
}

object RegularizerSum {
    implicit def toRegularizerSum(reqularizer: Regularizer): RegularizerSum = new RegularizerSum(Vector(reqularizer))
}