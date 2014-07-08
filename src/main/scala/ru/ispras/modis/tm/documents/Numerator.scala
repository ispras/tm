package ru.ispras.modis.tm.documents

import gnu.trove.procedure.TObjectIntProcedure

import scala.collection.mutable
import grizzled.slf4j.Logging
import gnu.trove.map.hash.{TObjectIntHashMap, TIntObjectHashMap}
import ru.ispras.modis.tm.attribute.{DefaultAttributeType, AttributeType}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 18:07
 */
/**
 * take into input sequence of textual documents, replace words by serial number, group words and return sequence of documents and
 * alphabet. Alphabet store map from number to words and vice versa
 */
object Numerator extends Logging {
    /**
     * @param textDocuments documents with words
     * @param rareTokenThreshold remove the words that occur less than rareTokenThreshold
     * @return documents with numbers, alphabet
     */
    def apply(textDocuments: Iterator[TextualDocument], rareTokenThreshold: Int = 0): (Seq[Document], Alphabet) = {
        val numberOfWords = mutable.Map[AttributeType, Int]().withDefaultValue(0)
        val wordsToNumber = mutable.Map[AttributeType, TObjectIntHashMap[String]]()
        var documentIndex = -1

        val (itForMapping, itForCnt) = textDocuments.duplicate
        val freqTokens = findFrequentTokens(itForCnt, rareTokenThreshold)

        val documents = itForMapping.map { case (textDocument) =>
            documentIndex += 1
            if (documentIndex % 1000 == 0) info("done " + documentIndex)
            processDocument(textDocument, numberOfWords, wordsToNumber, freqTokens, documentIndex)
        }.toVector
        info("numerator done")
        (documents, Alphabet(wordsToNumber.toMap))
    }

    /**
     * @param textDocuments text documents to be numerated
     * @param alphabet stays immutable, words not from the alphabet are ommited
     * @return
     */
    def apply(textDocuments: Iterator[TextualDocument], alphabet: Alphabet): Seq[Document] =
        (for ((doc, index) <- textDocuments.zipWithIndex) yield processDocument(doc, alphabet, index)).toVector

    private def processDocument(text: TextualDocument, alphabet: Alphabet, docIndex: Int): Document =
        new Document(text.attributeSet().map(attr => (attr, text.words(attr)))
            .map { case (attr, tokens) => attr -> tokens.map(w => alphabet.getIndex(attr, w)).flatten.groupBy(x => x).map { case (w, cnt) => (w, cnt.size.toShort)}.toSeq}.toMap, docIndex)

    /**
     * replace words by number in a single document for every attribute
     * @param textualDocument documents with words
     * @param numberOfWords map from attribute to max number of word, corresponding to this attribute
     * @param wordsToNumber is map from word(String) to serial number of this word(Int)
     * @param frequentWords set of words that should not be removed from the dataset
     * @param documentIndex serial number of document in collection
     * @return new Document
     */
    private def processDocument(textualDocument: TextualDocument,
                                numberOfWords: mutable.Map[AttributeType, Int],
                                wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                frequentWords: mutable.Map[AttributeType, mutable.Set[String]],
                                documentIndex: Int) = {
        val document = textualDocument.attributeSet().foldLeft(Map[AttributeType, Array[(Int, Short)]]()) {
            case (wordsInDocument, attribute) =>
                wordsInDocument.updated(attribute, replaceWordsByIndexes(textualDocument.words(attribute)
                    .filter(word => frequentWords(attribute).contains(word)), attribute, numberOfWords, wordsToNumber))
        }

        new Document(document.map { case (key, value) => (key, value.toSeq)}, documentIndex)
    }

    /**
     * replace words by serial number in a given document for given attribute
     * @param words sequence of words (String)
     * @param attribute type of words attribute
     * @param numberOfWords map from attribute to max number of word, corresponding to this attribute
     * @param wordsToNumber map from word(String) to serial number of this word(Int)
     * @return array (index of word, number of occurrence in given document)
     */
    private def replaceWordsByIndexes(words: Seq[String],
                                      attribute: AttributeType,
                                      numberOfWords: mutable.Map[AttributeType, Int],
                                      wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]]) = {
        val map = mutable.Map[Int, Short]().withDefaultValue(1)
        for (word <- words) {
            if (!wordsToNumber.contains(attribute)) wordsToNumber(attribute) = new TObjectIntHashMap[String]()
            if (!wordsToNumber(attribute).containsKey(word)) {
                wordsToNumber(attribute).put(word, numberOfWords(attribute))
                numberOfWords(attribute) += 1
            }
            map(wordsToNumber(attribute).get(word)) = (map(wordsToNumber(attribute).get(word)) + 1).toShort

        }
        map.toArray
    }

    private def findFrequentTokens(textDocuments: Iterator[TextualDocument], rareTokenThreshold: Int)
    : mutable.Map[AttributeType, mutable.Set[String]] = {

        /**
         * if rareTokenThreshold < 1, we don't want to remove any of words, so every word is
         * "frequent". In order to avoid unnecessary travecing over collection, we instantiate a set,
         * that includes every element.
         *
         * Yes, this set violates contract, but the only thing we need is contains(...) method
         */
        val allIncludingSet = new mutable.Set[String] {
            override def +=(elem: String): this.type = ???

            override def -=(elem: String): this.type = ???

            override def contains(elem: String): Boolean = true

            override def iterator: Iterator[String] = ???
        }

        if (rareTokenThreshold < 1) mutable.Map[AttributeType, mutable.Set[String]]().withDefaultValue(allIncludingSet)
        else {
            val counters = mutable.Map[AttributeType, TObjectIntHashMap[String]]()

            textDocuments.foreach(doc =>
                for ((attr, tokens) <- doc.attributes) {
                    if (!counters.contains(attr)) counters.put(attr, new TObjectIntHashMap[String]())

                    for (token <- tokens) counters(attr).adjustOrPutValue(token, 1, 1)
                }
            )


            counters.map { case (attr, map) =>
                val set = mutable.HashSet[String]()
                map.forEachEntry(new TObjectIntProcedure[String] {
                    override def execute(token: String, cnt: Int): Boolean = {
                        if (cnt > rareTokenThreshold) set += token
                        true
                    }
                })
                attr -> set
            }
        }
    }
}
