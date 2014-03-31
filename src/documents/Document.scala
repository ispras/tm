package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:42
 */
class Document(val attributes: Map[AttributeType, Array[(Int, Int)]]) {
    def getAttributes(attributeType: AttributeType): Array[(Int, Int)] = attributes.getOrElse(attributeType, Array[(Int, Int)]())
}
