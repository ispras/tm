package attribute

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 19:34
 */
class Word(val lang: String) extends AttributeType {
    override def toString() = lang

    override def equals(other: Any) = other match {
        case other: Word => lang == other.lang
        case _ => false
    }

    override def hashCode(): Int = super.hashCode() + 17 * lang.hashCode
}
