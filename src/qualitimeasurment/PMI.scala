package qualitimeasurment

import gnu.trove.map.TObjectFloatMap
import gnu.trove.map.hash.TIntFloatHashMap

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:37
 */
class PMI(private val unigrams: TIntFloatHashMap, private val bigrams: TObjectFloatMap[Array[Int]]) {

    private def getWordWeight(word: Int) = if(unigrams.containsKey(word)) unigrams.get(word) + 1f else 1f

    private def getBigramWeight(word: Int, otherWord: Int) = {
        val array = Array(word, otherWord).sorted
        if(bigrams.containsKey(array)) bigrams.get(array) + 1f else 1f
    }

    private def pairPMI(word: Int, otherWord: Int) = math.log(getWordWeight(word) * getWordWeight(otherWord) / getBigramWeight(word, otherWord))

    private def getTopWords(n: Int, topic: Array[Float]) = topic.zipWithIndex.sortBy(-_._1).take(n).map(_._2)
    // potom dopishu
}
