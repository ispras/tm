package scripts

import documents.{TextualDocument, Numerator}
import java.util.Random
import scala.io.Source
import java.io.File
import attribute.Category
import builder.{LDABuilder, PLSABuilder, RobustPLSABuilder}
import qualitimeasurment.{PMI, PrintTopics}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App {
    def getDocs(path: String) = {

        val lines = Source.fromFile(new File(path)).getLines().take(1000).map(line => new TextualDocument(Map(Category -> line.split(" "))))
        val random = new Random
        random.setSeed(13)
        val (docs, alphabet) = Numerator(lines.toSeq)

        val plsa = new RobustPLSABuilder(25, alphabet, docs, random, 33, 1.5f, 0.2f).build()
        (plsa, docs, alphabet)
    }


    val readDataTime = System.currentTimeMillis()
    val (plsa, docs, alphabet) = getDocs("/media/3d6a5a46-cbd3-49cd-abd4-2907eed0831a/home/padre/data/PigData/dde1000")

    println("number of words " + alphabet.numberOfWords()(Category))
    println(" readDataTime " + (System.currentTimeMillis() * 0.001 - readDataTime / 1000))
    val start = System.currentTimeMillis()
    val trainedModel = plsa.train(docs)

    println(trainedModel)

    PrintTopics.printAllTopics(10, trainedModel.phi(Category), alphabet)
    println(" time " + (System.currentTimeMillis() * 0.001 - start / 1000))


    val loadNGrams = System.currentTimeMillis()
    val pmi = PMI("/media/3d6a5a46-cbd3-49cd-abd4-2907eed0831a/home/padre/data/IM/PMI/unigram.short", "/media/3d6a5a46-cbd3-49cd-abd4-2907eed0831a/home/padre/data/IM/PMI/bigram.short", alphabet, 10, Category, " ")
    println(" loadNGrams " + (System.currentTimeMillis() * 0.001 - loadNGrams / 1000))

    val calculatePMI = System.currentTimeMillis()
    println(pmi.meanPMI(trainedModel.phi(Category)).map{case(topicId, value) => topicId + " -> " + value}.mkString("\n"))
    println(" calculatePMI " + (System.currentTimeMillis() * 0.001 - calculatePMI / 1000))

}
