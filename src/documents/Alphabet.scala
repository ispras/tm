package documents

import attribute.AttributeType
import gnu.trove.map.hash.{TObjectIntHashMap, TIntObjectHashMap}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:57
 */
/**
 * hold mapping form words index to words (string)
 * @param indexWordsMap attribute -> (index of word from it attribute -> word from it attribute)
 */
class Alphabet(private val indexWordsMap: Map[AttributeType, TIntObjectHashMap[String]],
               private val wordsIndexMap: Map[AttributeType, TObjectIntHashMap[String]]) {
    /**
     *
     * @param attribute words attribute
     * @param index index of words
     * @return words, corresponding to attribute and index
     */
    def apply(attribute: AttributeType, index: Int) = indexWordsMap(attribute).get(index)

    /**
     *
     * @return map attributeType -> number of unique words, corresponding to this attribute.
     *         Guarantee that words index < number of unique words
     */
    def numberOfWords(): Map[AttributeType, Int] = indexWordsMap.map{case (key, value) => (key, value.size)}

    /**
     *
     * @param attribute attribute type
     * @return number of words, corresponding to given attribute
     */
    def numberOfWords(attribute: AttributeType):  Int = numberOfWords()(attribute)

    /**
     * check if alphabet contain given word
     * @param attribute attribute of word
     * @param word input word
     * @return true if word in alphabet, false otherwise
     */
    def contain(attribute: AttributeType, word: String) = wordsIndexMap(attribute).contains(word)

    def getIndex(attribute: AttributeType, word: String) = {
        if (contain(attribute, word))  Some(wordsIndexMap(attribute).get(word)) else None
    }
}

object Alphabet {
    def apply(wordIndexMap: Map[AttributeType, TObjectIntHashMap[String]]) = {
        val indexWordMap = wordIndexMap.map{case(attribute, map) =>
            val reverseMap = new TIntObjectHashMap[String]()
            map.keys().foreach(key => reverseMap.put(map.get(key), key.toString))
            (attribute, reverseMap)
        }
        new Alphabet(indexWordMap, wordIndexMap)
    }
}