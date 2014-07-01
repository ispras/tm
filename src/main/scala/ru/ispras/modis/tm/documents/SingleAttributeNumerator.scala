package ru.ispras.modis.tm.documents

import ru.ispras.modis.tm.attribute.DefaultAttributeType

/**
 * Created by valerij on 7/1/14.
 *
 * this object may be used for the same purpose as Numerator if the data has only a single attribute type
 */
object SingleAttributeNumerator {
    /**
     * This method implicitly assigns DefaultAttributeType to the documents
     *
     * @param textDocuments iterator over tokenized documents (Seq[String] is a sequence of tokens)
     * @return documents with numbers, alphabet
     */
    def apply(textDocuments: Iterator[Seq[String]]): (Seq[Document], Alphabet) =
        Numerator.apply(textDocuments.map(tokens => new TextualDocument(Map(DefaultAttributeType -> tokens))))
}
