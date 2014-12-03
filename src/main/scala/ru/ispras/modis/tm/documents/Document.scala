package ru.ispras.modis.tm.documents

import ru.ispras.modis.tm.attribute.{DefaultAttributeType, AttributeType}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:42
 */
/**
 * this class hold document words for every attribute. Document may has more than one attribute. Words are grouped together
 * @param attributes map from attribute to words, corresponding to this attribute
 * @param serialNumber serial number of document in collection
 */
class Document(private val attributes: Map[AttributeType, Seq[(Int, Short)]], val serialNumber: Int) {
    /**
     * return words, corresponding to given attribute
     * @param attributeType type of attribute
     * @return array (index of word, number of occurrence in given document
     */
    def getAttributes(attributeType: AttributeType): Seq[(Int, Short)] = attributes.getOrElse(attributeType, Seq[(Int, Short)]())

    /**
     *
     * @return set of attributes included in the document
     */
    def attributeSet() = attributes.keySet

    def getWords = {
        require(attributes.contains(DefaultAttributeType) && attributeSet().size == 1, "use this method only in case of a single attribute -- DefaultAttributeType")
        attributes(DefaultAttributeType)
    }

    /**
     * check is given attribute presented in document
     * @param attribute type of attribute
     * @return true if there is words, corresponding to given attribute, false otherwise.
     */
    def contains(attribute: AttributeType) = attributes.contains(attribute)

    /**
     * total number of words, corresponds to any attribute in this document
     * @return int, number of words
     */
    def numberOfWords(): Int = attributes.values.foldLeft(0)((sumOneDocument, words) => sumOneDocument + words.map(_._2).sum)

}
