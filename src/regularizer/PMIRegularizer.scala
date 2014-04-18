package regularizer

import gnu.trove.map.TObjectFloatMap
import qualitimeasurment.{PMI, Bigram}
import attribute.AttributeType
import matrix.{ImmutableTheta, Theta, AttributedPhi}
import utils.TopicProcessing
import documents.Alphabet

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:21
 */
class PMIRegularizer(private val parameter: Float,
                     private val n: Int,
                     private val unigrams:  Array[Float],
                     private val bigrams: TObjectFloatMap[Bigram],
                     private val attribute: AttributeType) extends Regularizer {
    // extremely slow
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    def regularizePhi(phi: AttributedPhi, theta: ImmutableTheta) {
        if(phi.attribute == attribute) {
            var topicId = 0
            while(topicId < phi.numberOfRows){
                processOneTopic(topicId: Int, phi: AttributedPhi)
                topicId += 1
            }
        }
    }

    private def processOneTopic(topicId: Int, phi: AttributedPhi) = {
        val topWords = TopicProcessing.getTopWords(phi, topicId, n)
        var wordIndex = 0
        while(wordIndex < phi.numberOfColumns) {
            processOneWord(wordIndex, topicId, phi, topWords: Array[Int])
            wordIndex += 1
        }
    }

    private def processOneWord(wordId: Int, topicId: Int, phi: AttributedPhi, topWords: Array[Int]) {
        val weight = math.max(0, parameter * topWords.foldLeft(0d)((sum, otherWordId) => sum + phi.probability(topicId, otherWordId) * pmi(wordId, otherWordId)).toFloat)
        phi.addToExpectation(topicId, wordId, weight)
    }

    private def pmi(word: Int, otherWord: Int) = {
        math.log((bigrams.get(new Bigram(word, otherWord)) + 1e-15f) / unigrams(word) / unigrams(otherWord))
    }

    def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta) { }
}

object PMIRegularizer {
    def apply(pathToUnigrams: String, pathToBigrams: String, alphabet: Alphabet, parameter: Float, n: Int,  attribute: AttributeType, sep: String = ",") = {
        val unigramsMap = PMI.loadUnigrams(pathToUnigrams: String, alphabet: Alphabet, attribute: AttributeType, sep)
        val bigrams  = PMI.loadBigrams(pathToBigrams: String, alphabet: Alphabet, attribute: AttributeType, sep)
        val unigrams = (0 until alphabet.numberOfWords(attribute)).map{wordIndex => unigramsMap.get(wordIndex) + 1e-15f}.toArray
        new PMIRegularizer(parameter, n, unigrams, bigrams, attribute)
    }
}
