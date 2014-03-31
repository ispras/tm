package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 16:43
 */
class TextualDocument(val attributes: Map[AttributeType, Seq[String]]) {              // TODO private
    def getAttributes(attributeType: AttributeType): Seq[String] = attributes(attributeType)
}
