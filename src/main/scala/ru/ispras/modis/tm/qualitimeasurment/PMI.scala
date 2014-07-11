package ru.ispras.modis.tm.qualitimeasurment

import java.io.{BufferedInputStream, FileInputStream}
import java.util.zip.GZIPInputStream

import gnu.trove.map.TObjectFloatMap
import gnu.trove.map.hash.{TIntFloatHashMap, TObjectFloatHashMap}
import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Alphabet
import ru.ispras.modis.tm.matrix.AttributedPhi

import scala.io.Source

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:37
 */
/**
 * calculate pmi for given topics. pmi is correlated with topic coherence. See for example:
 * "Automatic Evaluation of Topic Coherence by David Newman et al."
 * @param unigrams map from word index to probability of word in train collection.
 * @param bigrams map from pair of word index to probability of pair to occur in slice window in train collection.
 * @param n number of top n words to calculate pmi
 * @param attribute main.scala.attribute type
 * @param epsilon add to every unigram and bigram weight to prevent division by zero
 */
class PMI(private val unigrams: TIntFloatHashMap,
          private val bigrams: TObjectFloatMap[Bigram],
          private val n: Int,
          private val attribute: AttributeType,
          private val epsilon: Float = 1e-15f) {


    /**
     *
     * @param word index of word
     * @return number of occurrence of words in trained collection + 1
     */
    private def getWordWeight(word: Int) = unigrams.get(word) + epsilon

    /**
     *
     * @param word index of the first word in bigram
     * @param otherWord index of the second word in bigram
     * @return  number of occurrence of bigram in trained collection + 1
     */
    private def getBigramWeight(word: Int, otherWord: Int) = bigrams.get(new Bigram(word, otherWord)) + epsilon


    /**
     * calculate pmi for two words
     * @param word the first word
     * @param otherWord the second word
     * @return pmi for pair of words
     */
    private def pairPMI(word: Int, otherWord: Int) = math.log(getBigramWeight(word, otherWord) / getWordWeight(word) / getWordWeight(otherWord))

    /**
     * return top most frequent word for given topic
     * @param topic array of words weight in topic
     * @return array of words indexes
     */
    private def getTopWords(topic: Array[Float]) = topic.zipWithIndex.sortBy(-_._1).take(n).map(_._2)

    /**
     *
     * @param words calculate pmi for every pair of word in given array.
     * @return array of pmi for every pair of words
     */
    private def pmi(words: Array[Int]) = words.flatMap(word => words.filter(_ != word).map(otherWord => pairPMI(word, otherWord)))

    /**
     * calculate the arithmetic mean of given array
     * @param array array of double
     * @return arithmetic mean of array elements
     */
    private def mean(array: Array[Double]) = array.sum / array.length

    /**
     * calculate median for input array
     * http://en.wikipedia.org/wiki/Median
     * @param array array of double
     * @return median of array
     */
    private def median(array: Array[Double]) = {
        if (array.length % 2 == 0)
            (array.sorted.apply(array.length / 2) + array.sorted.apply(array.length / 2 + 1)) / 2
        else
            array.sorted.apply(array.length / 2)
    }

    /**
     * calculate average pmi for given words. average may be mean or median
     * @param phi matrix of distribution of words by topic
     * @param average function to calculate a
     * @return average pmi for pairs of given words
     */
    private def averegePMI(phi: AttributedPhi, average: Array[Double] => Double) = {
        require(attribute == phi.attribute, "type of attribute in phi and in this does not correspond")
        (0 until phi.numberOfRows).map { topicIndex =>
            val topWords = getTopWords((0 until phi.numberOfColumns).map(wordIndex => phi.probability(topicIndex, wordIndex)).toArray)
            (topicIndex, average(pmi(topWords)))
        }.toArray
    }

    /**
     * calculate mean pmi for given words.
     * @param phi matrix of distribution of words by topic
     * @return mean pmi for pairs of given words
     */
    def meanPMI(phi: AttributedPhi) = averegePMI(phi, mean)

    /**
     * calculate median pmi for given words.
     * @param phi matrix of distribution of words by topic
     * @return median pmi for pairs of given words
     */
    def medianPMI(phi: AttributedPhi) = averegePMI(phi, median)
}

/**
 * companion object construct PMI from files with weight of bigrams and unigrams
 */
object PMI extends Logging {
    /**
     * build pmi from files with bigram and file with unigram. Order of words in bigram is not important, every unigram
     * and bigram should occure only once.
     * every string of file with unigram contain word and probability of word in train collection, separated  by sep
     * for example, if sep = ",":
     * my,0.1
     * shiny,0.2
     * ass,0.7
     * analogously for file with bigrams (sep = ","):
     * bite,my,0.1
     * my,ass,0.2
     * shiny,ass,0.7
     * @param pathToUnigrams path to file with unigrams
     * @param pathToBigrams path to file with bigrams
     * @param alphabet alphabet to map word to index
     * @param n number of top words to take to calculate pmi
     * @param attribute attribute type
     * @param sep separator in bigrams and unigrams file
     * @return instance of class PMI
     */
    def apply(pathToUnigrams: String, pathToBigrams: String, alphabet: Alphabet, n: Int, attribute: AttributeType, sep: String = ","): PMI = {
        val unigrams = loadUnigrams(pathToUnigrams: String, alphabet: Alphabet, attribute: AttributeType, sep)
        val bigrams = loadBigrams(pathToBigrams: String, alphabet: Alphabet, attribute: AttributeType, sep)
        new PMI(unigrams, bigrams, n, attribute)
    }

    /**
     *
     * @param pathToUnigrams path to file with unigrams
     * @param alphabet alphabet to map word to index
     * @param attribute attribute type
     * @param sep separator in bigrams file
     * @return trove map with unigrams weight
     */
    def loadUnigrams(pathToUnigrams: String, alphabet: Alphabet, attribute: AttributeType, sep: String) = {
        val map = new TIntFloatHashMap()
        val lines = getLines(pathToUnigrams)
        lines.map(_.split(sep)).filterNot(_.isEmpty).foreach { wordAndWeight =>
            val wordIndex = alphabet.getIndex(attribute, wordAndWeight(0))
            val weight = wordAndWeight(1).toFloat
            require(weight >= 0 && weight <= 1, "probability may not be greater than 1 and less than 0 ")
            if (wordIndex.nonEmpty) map.put(wordIndex.get, weight)
        }
        map
    }

    private def getLines(path: String) = {
        if (path.endsWith(".gz")) {
            Source.fromInputStream(new GZIPInputStream(new BufferedInputStream(
                new FileInputStream(path)))).getLines()
        }
        else {
            Source.fromFile(path).getLines()
        }
    }

    /**
     *
     * @param pathToBigrams path to file with bigrams
     * @param alphabet alphabet to map word to index
     * @param attribute attribute type
     * @param sep separator in bigrams  file
     * @return trove map from bigram (two words, replaced by serial number and placed in set) to the number of occurrence in collection
     */
    def loadBigrams(pathToBigrams: String, alphabet: Alphabet, attribute: AttributeType, sep: String) = {
        val map = new TObjectFloatHashMap[Bigram]()
        var done = 0
        val lines = getLines(pathToBigrams)

        lines.map(_.split(sep)).filterNot(_.isEmpty).foreach { wordsAndWeight =>
            val wordIndex = alphabet.getIndex(attribute, wordsAndWeight(0))
            val otherWordIndex = alphabet.getIndex(attribute, wordsAndWeight(1))
            val weight = wordsAndWeight(2).toFloat
            require(weight >= 0 && weight <= 1, "probability may not be greater than 1 and less than 0 ")
            if (wordIndex.nonEmpty && otherWordIndex.nonEmpty) map.put(new Bigram(wordIndex.get, otherWordIndex.get), weight)
            done += 1
            if (done % 100000 == 0) info(done)
        }
        map
    }
}



