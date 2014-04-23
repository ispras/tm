package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 16:43
 */
/**
 * hold words, corresponding to given attribute
 * @param attributes map from attribute to sequence of words
 */
class TextualDocument(private val attributes: Map[AttributeType, Seq[String]]) {
    require(attributes.values.forall(_.nonEmpty), "empty document")

    /**
     * return words, corresponding to given attribute
     * @param attributeType type of attribute
     * @return Array(word index, number of occurrence)
     */
    def words(attributeType: AttributeType): Seq[String] = attributes(attributeType)

    /**
     *
     * @return set of presented attributes
     */
    def attributeSet() = attributes.keySet
}
