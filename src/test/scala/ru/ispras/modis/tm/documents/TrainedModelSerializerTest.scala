package ru.ispras.modis.tm.documents

import org.scalatest.FunSuite
import ru.ispras.modis.tm.attribute.{DefaultAttributeType, Word}
import ru.ispras.modis.tm.documents.{Numerator, TextualDocument}
import ru.ispras.modis.tm.plsa.TrainedModelSerializer

/**
 * Created by valerij on 06.12.14.
 */
class TrainedModelSerializerTest extends FunSuite {
    test("alphabet serialization") {
        val doc1 = new TextualDocument(Map(Word("en") -> "my name is valeriy".split(' ').toSeq,
                                        Word("de") -> "ich heisse valeriy".split(' ').toSeq,
                                        DefaultAttributeType -> "а я валера".split(' ').toSeq))

        val (_, alphabet) = Numerator(Iterator(doc1))

        AlphabetSerializer.save(alphabet, "alphabet.bin")

        val alphabetDeserialized = AlphabetSerializer.load("alphabet.bin")

        assert(alphabet.indexWordsMap == alphabetDeserialized.indexWordsMap)
        assert(alphabet.wordsIndexMap == alphabetDeserialized.wordsIndexMap)

    }
}
