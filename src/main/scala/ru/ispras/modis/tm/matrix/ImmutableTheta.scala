package ru.ispras.modis.tm.matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 16:50
 */
class ImmutableTheta(theta: Theta) extends ImmutableOgre(theta)

object ImmutableTheta {
    implicit def toImmutableTheta(theta: Theta) = new ImmutableTheta(theta)
}
