package ru.ispras.modis.tm.documents

import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 16:43
 */
/**
 * holds words, related to the given attribute
 * @param attributes map from attribute to sequence of words
 */
class TextualDocument(private[documents] val attributes: Map[AttributeType, Seq[String]]) {
    require(attributes.values.forall(_.nonEmpty), "empty document")

    /**
     * return words, corresponding to given attribute
     * @param attributeType type of attribute
     * @return Array(word index, number of occurrence)
     */
    def words(attributeType: AttributeType): Seq[String] = attributes(attributeType)


    /**
     *
     * @return sequence of words
     */
    def words: Seq[String] = {
        require(attributes.contains(DefaultAttributeType), "Use this method only if your model contain only one attribute DefaultAttributeType")
        attributes(DefaultAttributeType)
    }

    /**
     *
     * @return set of presented attributes
     */
    def attributeSet() = attributes.keySet
}
