package qualitimeasurment

import matrix.AttributedPhi
import documents.Alphabet
import attribute.Category

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 07.04.14
 * Time: 14:30
 */
object PrintTopics {
    def extractTopic(phi: AttributedPhi, topicIndex: Int) = {
        (0 until phi.numberOfColumns).map(wordIndex => phi.probability(topicIndex, wordIndex)).toArray
    }

    def getTopWords(topic: Array[Float], n: Int) = topic.zipWithIndex.sortBy(-_._1).take(n).map(_._2)

    def printAllTopics(n: Int, phi: AttributedPhi, alphabet: Alphabet) {
        (0 until phi.numberOfRows).foreach{ topicIndex =>
            val topic = PrintTopics.extractTopic(phi, topicIndex)
            println(PrintTopics.getTopWords(topic, 12).map(word => alphabet.apply(phi.attribute, word)).mkString(" "))
        }
    }
}
