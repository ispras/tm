package ru.ispras.modis.tm.attribute

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 19:34
 */
final class Word(val lang: String) extends AttributeType {
    override def toString = "attribute.Word " + lang

    override def equals(other: Any) = other match {
        case other: Word => lang == other.lang
        case _ => false
    }

    override def hashCode(): Int = classOf[Word].hashCode() + 13 * lang.hashCode
}

object Word {
    def apply(lang: String) = new Word(lang)
}
