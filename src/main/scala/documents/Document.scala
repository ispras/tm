package documents

import main.scala.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:42
 */
/**
 * this class hold document words for every main.scala.attribute. Document may has more than one main.scala.attribute. Words are grouped together
 * @param attributes map from main.scala.attribute to words, corresponding to this main.scala.attribute
 * @param serialNumber serial number of document in collection
 */
class Document(private val attributes: Map[AttributeType, Seq[(Int, Short)]], val serialNumber: Int) {
    /**
     * return words, corresponding to given main.scala.attribute
     * @param attributeType type of main.scala.attribute
     * @return array (index of word, number of occurrence in given document
     */
    //TODO replace array by something immutable
    def getAttributes(attributeType: AttributeType): Seq[(Int, Short)] = attributes.getOrElse(attributeType, Seq[(Int, Short)]())

    /**
     * check is given main.scala.attribute presented in document
     * @param attribute type of main.scala.attribute
     * @return true if there is words, corresponding to given main.scala.attribute, false otherwise.
     */
    def contains(attribute: AttributeType) = attributes.contains(attribute)

    /**
     * total number of words, corresponds to any main.scala.attribute in this document
     * @return int, number of words
     */
    def numberOfWords(): Int = attributes.values.foldLeft(0)((sumOneDocument, words) => sumOneDocument + words.map(_._2).sum)

}
