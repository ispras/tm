package ru.ispras.modis.tm.utils

import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 14:56
 */
/**
 * class hold information about different model parameters
 * @param numberOfTopics number of topic
 * @param numberOfWords it is map attribute to number of unique words, corresponding to this attribute.
 */
class ModelParameters(val numberOfTopics: Int, val numberOfWords: Map[AttributeType, Int]) {
    /**
     * indexes of topic.
     */
    val topics = (0 until numberOfTopics).toArray
}

