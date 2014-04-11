package documents

import scala.collection.mutable
import attribute.{AttributeType}
import grizzled.slf4j.Logging
import gnu.trove.map.hash.{TObjectIntHashMap, TIntObjectHashMap}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 18:07
 */
object Numerator extends Logging {
    def apply(textDocuments: Iterator[TextualDocument]): (Seq[Document], Alphabet) = {
        //val alphabet = mutable.Map[AttributeType, TIntObjectHashMap[String]]()
        val numberOfWords = mutable.Map[AttributeType, Int]().withDefaultValue(0)
        val wordsToNumber = mutable.Map[AttributeType, TObjectIntHashMap[String]]()
        var documentIndex = -1
        val documents = textDocuments.map {case(textDocument) =>
                 documentIndex += 1
                if (documentIndex % 1000 == 0) info("done " + documentIndex)
                processDocument(textDocument, numberOfWords, wordsToNumber, documentIndex)
        }.toVector
        info("numerator done")
        (documents, Alphabet(wordsToNumber.toMap))

    }

    private def processDocument(textualDocument: TextualDocument,
                                numberOfWords: mutable.Map[AttributeType, Int],
                                wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                documentIndex: Int) = {
        val document = textualDocument.attributeSet().foldLeft(Map[AttributeType, Array[(Int, Short)]]()) {
            case (wordsInDocument, attribute) =>
                wordsInDocument.updated(attribute, replaceWordsByIndexes(textualDocument.words(attribute), attribute, numberOfWords, wordsToNumber))
        }

        new Document(document, documentIndex)
    }

    private def replaceWordsByIndexes(words: Seq[String],
                                      attribute: AttributeType,
                                      numberOfWords: mutable.Map[AttributeType, Int],
                                      wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]]) = {
        val map = mutable.Map[Int, Short]().withDefaultValue(1)
        for (word <- words) {
            if(!wordsToNumber.contains(attribute)) wordsToNumber(attribute) = new TObjectIntHashMap[String]()
            if (!wordsToNumber(attribute).containsKey(word)) {
                wordsToNumber(attribute).put(word, numberOfWords(attribute))
                numberOfWords(attribute) += 1
            }
            map(wordsToNumber(attribute).get(word)) = (map(wordsToNumber(attribute).get(word)) + 1).toShort

        }
        map.toArray
    }
}
