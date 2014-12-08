package ru.ispras.modis.tm.bigartm

import java.io.{File, FileWriter, BufferedWriter}

import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.documents.{Alphabet, Document}

/**
 * Created by valerij on 12/5/14.
 */
object WriteDatasetForBigARTM {
    def apply(documents: Array[Document], alphabet : Alphabet, pathForDocs: String , pathForVocab: String) : Unit = {
        val docWriter = new BufferedWriter(new FileWriter(new File(pathForDocs )))
        val vocabWriter = new BufferedWriter(new FileWriter(new File(pathForVocab )))

        docWriter.write(documents.length + "\n")
        docWriter.write(alphabet.numberOfWords(DefaultAttributeType) + "\n")
        docWriter.write(documents.map(_.getAttributes(DefaultAttributeType).length).sum + "\n")
        for (doc <- documents; (word, nwd) <- doc.getAttributes(DefaultAttributeType)) docWriter.write((doc.serialNumber + 1) + " " + (word + 1)  + " " + nwd + "\n")

        docWriter.close()

        for (wordIndex <- 0 until alphabet.numberOfWords(DefaultAttributeType)) vocabWriter.write(alphabet(wordIndex) + "\n")

        vocabWriter.close()
    }
}
