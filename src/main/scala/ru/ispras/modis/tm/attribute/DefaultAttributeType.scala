package ru.ispras.modis.tm.attribute

/**
 * Created by valerij on 7/1/14.
 */
/**
 * It's used implicitly if user has only a single Attribute Type and does not explicitly specify it
 */
object DefaultAttributeType extends AttributeType {
    override def equals(other: Any) = this.getClass == other.getClass

    override def hashCode(): Int = this.getClass.hashCode()
}
