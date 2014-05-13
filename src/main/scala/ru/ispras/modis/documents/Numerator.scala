package ru.ispras.modis.documents

import scala.collection.mutable
import grizzled.slf4j.Logging
import gnu.trove.map.hash.{TObjectIntHashMap, TIntObjectHashMap}
import ru.ispras.modis.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 18:07
 */
/**
 * take into input sequence of textual main.scala.documents, replace words by serial number, group words and return sequence of main.scala.documents and
 * alphabet. Alphabet store map from number to words and vice versa
 */
object Numerator extends Logging {
    /**
      * @param textDocuments main.scala.documents with words
      * @return main.scala.documents with numbers, alphabet
      */
    def apply(textDocuments: Iterator[TextualDocument]): (Seq[Document], Alphabet) = {
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

    /**
     * replace words by number in a single document for every main.scala.attribute
     * @param textualDocument main.scala.documents with words
     * @param numberOfWords map from main.scala.attribute to max number of word, corresponding to this main.scala.attribute
     * @param wordsToNumber is map from word(String) to serial number of this word(Int)
     * @param documentIndex serial number of document in collection
     * @return new Document
     */
    private def processDocument(textualDocument: TextualDocument,
                                numberOfWords: mutable.Map[AttributeType, Int],
                                wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                documentIndex: Int) = {
        val document = textualDocument.attributeSet().foldLeft(Map[AttributeType, Array[(Int, Short)]]()) {
            case (wordsInDocument, attribute) =>
                wordsInDocument.updated(attribute, replaceWordsByIndexes(textualDocument.words(attribute), attribute, numberOfWords, wordsToNumber))
        }

        new Document(document.map{case(key, value) => (key, value.toSeq)}, documentIndex)
    }

    /**
     * replace words by serial number in a given document for given main.scala.attribute
     * @param words sequence of words (String)
     * @param attribute type of words main.scala.attribute
     * @param numberOfWords map from main.scala.attribute to max number of word, corresponding to this main.scala.attribute
     * @param wordsToNumber map from word(String) to serial number of this word(Int)
     * @return array (index of word, number of occurrence in given document)
     */
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
