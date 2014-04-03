package documents

import attribute.AttributeType
import gnu.trove.map.hash.TIntObjectHashMap

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:57
 */
/**
 * hold mapping form words index to words (string)
 * @param wordsMap attribute -> (index of word from it attribute -> word from it attribute)
 */
class Alphabet(private val wordsMap: Map[AttributeType, TIntObjectHashMap[String]]) {
    /**
     *
     * @param attribute words attribute
     * @param index index of words
     * @return words, corresponding to attribute and index
     */
    def apply(attribute: AttributeType, index: Int) = wordsMap(attribute).get(index)

    /**
     *
     * @return map attributeType -> number of unique words, corresponding to this attribute.
     *         Guarantee that words index < number of unique words
     */
    def numberOfWords() = wordsMap.map{case (key, value) => (key, value.size)}
}
