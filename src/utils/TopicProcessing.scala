package utils

import matrix.AttributedPhi

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:26
 */
object TopicProcessing {
    //TODO replace by heap
    def getTopWords(phi: AttributedPhi, topicId: Int, n: Int) = (0 until phi.numberOfColumns).map{
        wordId => (wordId, phi.probability(topicId, wordId))
    }.sortBy{case(wordId, probability) => -probability}.take(n).map{case(wordId, probability) => wordId}.toArray
}
