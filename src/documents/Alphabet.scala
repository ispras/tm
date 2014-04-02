package documents

import attribute.AttributeType
import gnu.trove.map.hash.TIntObjectHashMap

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:57
 */
class Alphabet(val wordsMap: Map[AttributeType, TIntObjectHashMap[String]]) {
    def apply(attribute: AttributeType, index: Int) = wordsMap(attribute).get(index)
}
