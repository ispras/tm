package ru.ispras.modis.tm.documents

import gnu.trove.map.hash.{TIntIntHashMap, TObjectIntHashMap}
import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.utils.TupleArraySeq

import scala.collection.mutable

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
    def apply(textDocuments: Iterator[TextualDocument], rareTokenThreshold: Int = 0): (Array[Document], Alphabet) = {
        val numberOfWords = mutable.Map[AttributeType, Int]().withDefaultValue(0)
        val wordsToNumber = mutable.Map[AttributeType, TObjectIntHashMap[String]]()
        val freqTokens = mutable.Map[AttributeType, TIntIntHashMap]()
        var documentIndex = -1

        val documents = textDocuments.map { case (textDocument) =>
            documentIndex += 1
            if (documentIndex % 1000 == 0) info("done " + documentIndex)
            processDocument(textDocument, numberOfWords, wordsToNumber, documentIndex, freqTokens)
        }.toArray

        if(rareTokenThreshold > 0) removeRareTokens(documents, freqTokens, wordsToNumber, rareTokenThreshold)
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

    /**
     * replace words by index in given document with fixed alphabet (i.e. we read the main part of the
     * collection and want to add a few documents) All words not in alphabet are omited
     * @param textualDocument documents with words
     * @param alphabet map from words to index
     * @param docIndex serial number of document in collection
     * @return  new Document
    */

    private def processDocument(textualDocument: TextualDocument, alphabet: Alphabet, docIndex: Int): Document =
        new Document(textualDocument.attributeSet().map(attr => (attr, textualDocument.words(attr)))
            .map { case (attr, tokens) =>
                attr -> TupleArraySeq(tokens.map(w => alphabet.getIndex(attr, w)).flatten.groupBy(x => x).map { case (w, cnt) => (w, cnt.size)}.toArray)
            }.toMap,
            docIndex)

    /**
     * replace words by number in a single document for every attribute
     * @param textualDocument documents with words
     * @param numberOfWords map from attribute to max number of word, corresponding to this attribute
     * @param wordsToNumber is map from word(String) to serial number of this word(Int)
     * @param documentIndex serial number of document in collection
     * @return new Document
     */
    private def processDocument(textualDocument: TextualDocument,
                                numberOfWords: mutable.Map[AttributeType, Int],
                                wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                documentIndex: Int,
                                freqTokens: mutable.Map[AttributeType, TIntIntHashMap]) = {
        val document = textualDocument.attributeSet().foldLeft(Map[AttributeType, Array[(Int, Int)]]()) {
            case (wordsInDocument, attribute) =>
                wordsInDocument.updated(attribute, replaceWordsByIndexes(textualDocument.words(attribute), attribute, numberOfWords, wordsToNumber, freqTokens))
        }

        new Document(document.map { case (key, value) => (key, TupleArraySeq(value))}, documentIndex)
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
                                      wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                      freqTokens: mutable.Map[AttributeType, TIntIntHashMap]) = {

        if (!freqTokens.contains(attribute)) freqTokens(attribute) = new TIntIntHashMap()
        if (!wordsToNumber.contains(attribute)) wordsToNumber(attribute) = new TObjectIntHashMap[String]()

        val map = mutable.Map[Int, Int]().withDefaultValue(0)
        for (word <- words) {
            if (!wordsToNumber(attribute).containsKey(word)) {
                wordsToNumber(attribute).put(word, numberOfWords(attribute))
                freqTokens(attribute).put(numberOfWords(attribute), 0)
                numberOfWords(attribute) += 1
            }
            val wordId = wordsToNumber(attribute).get(word)
            map(wordId) = (map(wordId) + 1)
            freqTokens(attribute).increment(wordId)
        }
        map.toArray
    }

    private def removeRareTokens(documents: Array[Document],
                                 freqTokens: mutable.Map[AttributeType, TIntIntHashMap],
                                 wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                 threshold: Int) = {


        modifyFreqTokenMap(freqTokens, threshold)
        0.until(documents.length).foreach(docId => documents(docId) = removeRareTokenInSingleDocument(documents(docId), freqTokens))
        modifyWordsToNumber(wordsToNumber, freqTokens)
    }

    private def modifyFreqTokenMap(freqTokens: mutable.Map[AttributeType, TIntIntHashMap], threshold: Int) {
        freqTokens.foreach{case(attribute, map) =>
            map.keys().foldLeft(0){(nonRareIndex, key) =>
                if (map.get(key) <= threshold) {map.put(key, -1); nonRareIndex}
                else {map.put(key, nonRareIndex); nonRareIndex + 1}
            }
        }
    }

    private def removeRareTokenInSingleDocument(document: Document, freqTokens: mutable.Map[AttributeType, TIntIntHashMap]) = {
        val filteredWords = document.attributeSet().map{attribute =>
            val words = document.getAttributes(attribute)
            attribute -> TupleArraySeq(words.map{case(wordId, wordNum) => (freqTokens(attribute).get(wordId), wordNum)}.filter(_._1 >= 0).toArray)
        }.toMap
        new Document(filteredWords, document.serialNumber)
    }

    private def modifyWordsToNumber(wordsToNumber: mutable.Map[AttributeType, TObjectIntHashMap[String]],
                                    freqTokens: mutable.Map[AttributeType, TIntIntHashMap] ) {
        wordsToNumber.keys.foreach(attribute => modifyWordsToNumberGivenAttribute(attribute, wordsToNumber(attribute), freqTokens(attribute)))
    }

    private def modifyWordsToNumberGivenAttribute(attribute: AttributeType,
                                                  words2Number: TObjectIntHashMap[String],
                                                  freqTokens: TIntIntHashMap) {
        words2Number.keys().foreach{word =>
            val wordNewId = freqTokens.get(words2Number.get(word.toString))
            if (wordNewId >= 0) words2Number.put(word.toString, wordNewId)
            else words2Number.remove(word.toString)
        }
    }



}
