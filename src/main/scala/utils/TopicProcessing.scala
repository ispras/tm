package utils

import matrix.{Ogre, AttributedPhi}
import documents.Alphabet
import java.io.FileWriter
import attribute.Category


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:26
 */
object TopicProcessing {
    def getTopWords(phi: AttributedPhi, topicId: Int, n: Int) = (0 until phi.numberOfColumns).map{
        wordId => (wordId, phi.probability(topicId, wordId))
    }.sortBy{case(wordId, probability) => -probability}.take(n).map{case(wordId, probability) => wordId}.toArray

    def printAllTopics(n: Int, phi: AttributedPhi, alphabet: Alphabet) {
        (0 until phi.numberOfRows).foreach{ topicIndex =>
            println(getTopWords(phi, topicIndex, n).map(word => alphabet.apply(phi.attribute, word)).mkString(" "))
        }
    }

    def saveMatrix(path: String, matrix: Ogre) {
        val out = new FileWriter(path)
        val phi = 0.until(matrix.numberOfRows).map{topicId =>
            0.until(matrix.numberOfColumns).map(wordId => matrix.probability(topicId, wordId)).mkString(", ")
        }.mkString("\n")
        out.write(phi)
        out.close()
    }
}