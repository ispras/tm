package documents

import scala.collection.mutable
import attribute.{Category, AttributeType}
import plsa.PLSAFactory
import initialapproximationgenerator.RandomInitialApproximationGenerator
import java.util.Random
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import grizzled.slf4j.Logging
import gnu.trove.map.hash.TIntObjectHashMap

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 18:07
 */
object Numerator extends Logging {
    def apply(textDocuments: Seq[TextualDocument]): (Seq[Document], Alphabet) = {
        val alphabet = mutable.Map[AttributeType, TIntObjectHashMap[String]]()
        val numberOfWords = mutable.Map[AttributeType, Int]().withDefaultValue(0)
        val wordsToNumber = mutable.Map[(AttributeType, String), Int]()
        val documents = textDocuments.toVector.zipWithIndex.map {case(textDocument, documentIndex) =>
                if (documentIndex % 1000 == 0) info("done " + documentIndex)
                processDocument(textDocument, numberOfWords, alphabet, wordsToNumber, documentIndex)
        }
        (documents, new Alphabet(alphabet.toMap))

    }

    private def processDocument(textualDocument: TextualDocument,
                                numberOfWords: mutable.Map[AttributeType, Int],
                                alphabet: mutable.Map[AttributeType, TIntObjectHashMap[String]],
                                wordsToNumber: mutable.Map[(AttributeType, String), Int],
                                documentIndex: Int) = {
        val document = textualDocument.attributeSet().foldLeft(Map[AttributeType, Array[(Int, Int)]]()) {
            case (wordsInDocument, attribute) =>
                wordsInDocument.updated(attribute, replaceWordsByIndexes(textualDocument.words(attribute), attribute, numberOfWords, alphabet, wordsToNumber))
        }

        new Document(document, documentIndex)
    }

    private def replaceWordsByIndexes(words: Seq[String],
                                      attribute: AttributeType,
                                      numberOfWords: mutable.Map[AttributeType, Int],
                                      alphabet: mutable.Map[AttributeType, TIntObjectHashMap[String]],
                                      wordsToNumber: mutable.Map[(AttributeType, String), Int]) = {
        val map = mutable.Map[Int, Int]().withDefaultValue(0)
        if (!alphabet.contains(attribute)) alphabet(attribute) = new TIntObjectHashMap[String]()
        for (word <- words) {
            if (!wordsToNumber.contains((attribute, word))) {
                wordsToNumber((attribute, word)) = numberOfWords(attribute)
                alphabet(attribute).put(numberOfWords(attribute), word)
                numberOfWords(attribute) += 1
            }
            map(wordsToNumber((attribute, word))) += 1
        }
        map.toArray
    }
}

object Test extends App {
    val td1 = new TextualDocument(Map(Category -> List("ducks", "ducks", "ducks", "ducks")))
    val td2 = new TextualDocument(Map(Category -> List("boobs", "boobs", "boobs", "boobs")))
    val td3 = new TextualDocument(Map(Category -> List("boobs", "ducks", "boobs", "ducks")))
    val td4 = new TextualDocument(Map(Category -> List("boobs", "boobs", "boobs", "ducks")))
    val td5 = new TextualDocument(Map(Category -> List("boobs", "ducks", "ducks", "ducks")))
    val (docs, alphabet) = Numerator.apply(List(td1, td2, td3, td4, td5))
    val random = new Random
    val plsa = PLSAFactory(new RandomInitialApproximationGenerator(random),
        new ZeroRegularizer(),
        docs,
        2,
        new ZeroSparsifier(),
        new ZeroSparsifier(),
        new MaxNumberOfIterationStoppingCriteria(33),
        alphabet)

    println(alphabet.wordsMap)
    val trainedModel = plsa.train(docs)
    println(trainedModel.theta + "\n")
    println(trainedModel.phi(Category))

}
