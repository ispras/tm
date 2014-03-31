package documents

import scala.collection.mutable
import attribute.{Category, AttributeType}
import plsa.PLSAFactory
import initialapproximationgenerator.RandomInitialApproximationGenerator
import java.util.Random
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import stoppingcriteria.MaxNumberOfIteration

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 18:07
 */
object Numerator {
    def apply(textDocuments: Seq[TextualDocument]): (Alphabet, Seq[Document]) = {
        var numOfDocsDone = 0
        val alphabet = mutable.Map[AttributeType, Map[Int, String]]().withDefaultValue(Map[Int, String]())
        val maxWordNum = mutable.Map[AttributeType, Int]().withDefaultValue(0)
        val wordsToNumber = mutable.Map[(AttributeType, String), Int]()
        val documents = textDocuments.toArray.map {
            textDocument =>
                numOfDocsDone += 1
                if (numOfDocsDone % 1000 == 0) println("done " + numOfDocsDone)
                processOneDocument(textDocument, maxWordNum, alphabet, wordsToNumber)
        }
        (new Alphabet(alphabet.toMap), documents.toVector)

    }

    private def processOneDocument(textualDocument: TextualDocument,
                                   maxWordNum: mutable.Map[AttributeType, Int],
                                   alphabet: mutable.Map[AttributeType, Map[Int, String]],
                                   wordsToNumber: mutable.Map[(AttributeType, String), Int]) = {
        require(textualDocument.attributes.values.exists(_.nonEmpty), "empty document")
        val document = textualDocument.attributes.foldLeft(Map[AttributeType, Array[(Int, Int)]]()) {
            case (wordsInDocument, (attribute, words)) =>
                wordsInDocument.updated(attribute, replaceWordsByNumber(words, attribute, maxWordNum, alphabet, wordsToNumber))
        }

        new Document(document)
    }

    private def replaceWordsByNumber(words: Seq[String],
                                     attribute: AttributeType,
                                     maxWordNum: mutable.Map[AttributeType, Int],
                                     alphabet: mutable.Map[AttributeType, Map[Int, String]],
                                     wordsToNumber: mutable.Map[(AttributeType, String), Int]) = {
        val map = mutable.Map[Int, Int]().withDefaultValue(0)
        words.foreach {
            word =>
                if (!wordsToNumber.contains((attribute, word))) {
                    wordsToNumber((attribute, word)) = maxWordNum(attribute)
                    alphabet(attribute) = alphabet(attribute) + (maxWordNum(attribute) -> word)
                    maxWordNum(attribute) += 1
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
    val (alphabet, docs) = Numerator.apply(List(td1, td2, td3, td4, td5))
    val random = new Random
    val plsa = PLSAFactory(new RandomInitialApproximationGenerator(random),
        new ZeroRegularizer(),
        docs,
        2,
        new ZeroSparsifier(),
        new ZeroSparsifier(),
        new MaxNumberOfIteration(33),
        alphabet)

    println(alphabet.wordsMap)
    val trainedModel = plsa.train(docs)
    println(trainedModel.theta + "\n")
    println(trainedModel.phi(Category))

}
