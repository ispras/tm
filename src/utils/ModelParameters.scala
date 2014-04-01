package utils

import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 14:56
 */
class ModelParameters(val numberOfTopics: Int, val numberOfWords: Map[AttributeType, Int]) {
    val topics = (0 until numberOfTopics).toArray
}

