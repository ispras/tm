package ru.ispras.modis.tm.utils

import ru.ispras.modis.tm.matrix.{Ogre, AttributedPhi}
import ru.ispras.modis.tm.documents.Alphabet
import java.io.{File, FileWriter}
import ru.ispras.modis.tm.attribute.Category

import scala.io.Source


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:26
 */
object TopicHelper {
    /**
     *
     * @param phi matrix of distribution of words by topics
     * @param topicId index of topic
     * @param n number of words to return
     * @return ids of n most popular words from given topic
     */
    def getTopWords(phi: AttributedPhi, topicId: Int, n: Int) = (0 until phi.numberOfColumns).map {
        wordId => (wordId, phi.probability(topicId, wordId))
    }.sortBy { case (wordId, probability) => -probability}.take(n).map { case (wordId, probability) => wordId}.toArray

    /**
     * print top n most popular words (words with the highest probability) from each topic
     * (it may be useful to estimate topic coherence)
     * @param n number of words to return
     * @param phi matrix of distribution of words by topics
     * @param alphabet map from words to it's index and vice versa
     */
    def printAllTopics(n: Int, phi: AttributedPhi, alphabet: Alphabet) {
        (0 until phi.numberOfRows).foreach { topicIndex =>
            println(getTopWords(phi, topicIndex, n).map(word => alphabet.apply(phi.attribute, word)).mkString(" "))
        }
    }

    /**
     * save matrix (Phi - words by topic, Theta - document by topic or any other Ogre) in txt file in csv format.
     * Each row represent the row of matrix, values are seporated by ", "
     * @param path path to file where you wont to save matrix
     * @param matrix matrix that you want to save (for example Theta)
     */
    def saveMatrix(path: String, matrix: Ogre) {
        val out = new FileWriter(path)
        val phi = 0.until(matrix.numberOfRows).map { topicId =>
            0.until(matrix.numberOfColumns).map(wordId => matrix.probability(topicId, wordId)).mkString(", ")
        }.mkString("\n")
        out.write(phi)
        out.close()
    }


    /**
     * load matrix from txt file, see description of format in saveMatrix
     * @param path path to file with matrix
     * @return matrix in Array[Array] format
     */
    def loadMatrix(path: String): Array[Array[Float]] =
        Source.fromFile(new File(path)).getLines().map(_.split(", ").map(_.toFloat)).toArray

    /**
     * this method copy values of matrix to Array[Array]
     * @param matrix matrix to extract values
     * @return copy of stocastic matrix in Array[Array] format
     */
    def copyMatrixToArray(matrix: Ogre): Array[Array[Float]] = 0.until(matrix.numberOfRows).map(row =>
        0.until(matrix.numberOfColumns).map(column => matrix.probability(row, column)).toArray).toArray
}
