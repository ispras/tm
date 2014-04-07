package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:42
 */
class Document(private val attributes: Map[AttributeType, Array[(Int, Int)]], val serialNumber: Int) {
    def getAttributes(attributeType: AttributeType): Array[(Int, Int)] = attributes.getOrElse(attributeType, Array[(Int, Int)]())

    def contains(attribute: AttributeType) = attributes.contains(attribute)

    def numberOfWords() = attributes.values.foldLeft(0)((sumOneDocument, words) => sumOneDocument + words.map(_._2).sum)

}
