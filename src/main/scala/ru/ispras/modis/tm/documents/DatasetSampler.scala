package ru.ispras.modis.tm.documents

import java.util.Random

/**
 * Created by valerij on 07.12.14.
 */
class DatasetSampler(private val random : Random) {
    def apply(documents : Array[Document], fraction : Double) : Array[Document] = {
        documents.zip(( 0 until documents.size).map(_ => random.nextDouble())).filter{case(doc, rnd) => rnd < fraction}.zipWithIndex.map{case((doc, _), index) =>
            new Document(doc.attributes, index)
        }
    }
}
