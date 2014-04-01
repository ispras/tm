package documents

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 17:57
 */
class Alphabet(val wordsMap: Map[AttributeType, Map[Int, String]]) {
    // TODO trove
    def apply(attribute: AttributeType, index: Int) = wordsMap(attribute)(index)
}
