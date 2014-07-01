package ru.ispras.modis.utils

import ru.ispras.modis.matrix.{Ogre, AttributedPhi}
import ru.ispras.modis.documents.Alphabet
import java.io.{File, FileWriter}
import ru.ispras.modis.attribute.Category

import scala.io.Source


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:26
 */
object TopicHelper {
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

    /**
     *
     * @param path
     * @return matrix of size numberOfTopics*NumberOfWords
     */
    def loadMatrix(path: String): Array[Array[Float]] =
        Source.fromFile(new File(path)).getLines().map(_.split(", ").map(_.toFloat)).toArray
}
