package ru.ispras.modis.tm.documents

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}
import ru.ispras.modis.tm.utils.DefaultChecker

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
class TextualDocument(private[documents] val attributes: Map[AttributeType, Seq[String]]) extends DefaultChecker with Logging{
    if(!attributes.values.forall(_.nonEmpty)) warn("empty document")

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
        checkDefault(attributes)
        attributes(DefaultAttributeType)
    }

    /**
     *
     * @return set of presented attributes
     */
    def attributeSet() = attributes.keySet
}
