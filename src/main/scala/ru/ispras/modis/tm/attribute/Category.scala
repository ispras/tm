package ru.ispras.modis.tm.attribute

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 19:32
 */
object Category extends AttributeType {
    override def equals(other: Any) = this.getClass == other.getClass

    override def hashCode(): Int = this.getClass.hashCode()
}