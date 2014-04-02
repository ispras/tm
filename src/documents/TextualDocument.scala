package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 16:43
 */
class TextualDocument(private val attributes: Map[AttributeType, Seq[String]]) {
    require(attributes.values.exists(_.nonEmpty), "empty document")
    def words(attributeType: AttributeType): Seq[String] = attributes(attributeType)

    def attributeSet() = attributes.keySet
}
