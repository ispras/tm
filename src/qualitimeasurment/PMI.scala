package qualitimeasurment

import gnu.trove.map.TObjectFloatMap
import gnu.trove.map.hash.TIntFloatHashMap
import matrix.AttributedPhi
import documents.Alphabet

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:37
 */
class PMI(private val unigrams: TIntFloatHashMap, private val bigrams: TObjectFloatMap[Array[Int]], private val n: Int) {

    private def getWordWeight(word: Int) = if(unigrams.containsKey(word)) unigrams.get(word) + 1f else 1f

    private def getBigramWeight(word: Int, otherWord: Int) = {
        val array = Array(word, otherWord).sorted
        if(bigrams.containsKey(array)) bigrams.get(array) + 1f else 1f
    }

    private def pairPMI(word: Int, otherWord: Int) = math.log(getWordWeight(word) * getWordWeight(otherWord) / getBigramWeight(word, otherWord))

    private def getTopWords(topic: Array[Float]) = topic.zipWithIndex.sortBy(-_._1).take(n).map(_._2)

    private def pmi(words: Array[Int]) = words.flatMap(word => words.filter(_ != word).map(otherWord => pairPMI(word, otherWord)))

    private def mean(array: Array[Double]) = array.sum / array.length

    private def median(array: Array[Double]) = {
        if(array.length % 2 == 0)
            (array.sorted.apply(array.length / 2) + array.sorted.apply(array.length / 2 + 1)) / 2
        else
            array.sorted.apply(array.length / 2)
    }

    private def averegePMI(phi: AttributedPhi, average:Array[Double] => Double) = {
        (0 until phi.numberOfRows).map{ topicIndex =>
            val topWords = getTopWords((0 until phi.numberOfColumns).map(wordIndex => phi.probability(topicIndex, wordIndex)).toArray)
            (topicIndex, average(pmi(topWords)))
        }.toArray
    }

    def meanPMI(phi: AttributedPhi) = averegePMI(phi, mean)

    def medianPMI(phi: AttributedPhi) = averegePMI(phi, median)
}



