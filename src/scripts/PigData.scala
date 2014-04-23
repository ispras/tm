package scripts

import documents.{TextualDocument, Numerator}
import java.util.Random
import scala.io.Source
import java.io.File
import attribute.Category
import builder.{PLSAWithPMI, LDABuilder, PLSABuilder, RobustPLSABuilder}
import qualitimeasurment.{PMI}
import utils.TopicProcessing

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App {

    def getDocs(path: String, param: Float) = {

        val lines = Source.fromFile(new File(path)).getLines().take(10).map(line => new TextualDocument(Map(Category -> line.split(" "))))
        val random = new Random
        random.setSeed(13)
        val (docs, alphabet) = Numerator(lines)

        /*val plsa = new RobustPLSABuilder(25,
            alphabet,
            docs,
            random,
            33,
            0.5f,
            0.5f).build()
            */
        val plsa = new PLSAWithPMI(25,
            alphabet,
            docs,
            random,
            33,
            "/home/padre/arxiv/arxiv.unigram",
            "/home/padre/arxiv/arxiv.bigram.fltr10000.gz",
            0.1f,
            10,
            Category,
            " ").build()
        (plsa, docs, alphabet)
    }

    def doIt(param: Float) {
        val readDataTime = System.currentTimeMillis()
        val (plsa, docs, alphabet) = getDocs("/home/padre/arxiv/arxiv.prepr", param)

        println("number of words " + alphabet.numberOfWords()(Category))
        println(" readDataTime " + (System.currentTimeMillis() * 0.001 - readDataTime / 1000))
        val start = System.currentTimeMillis()
        val trainedModel = plsa.train(docs)

        println(trainedModel)

        TopicProcessing.printAllTopics(10, trainedModel.phi(Category), alphabet)
        println("train time " + (System.currentTimeMillis() * 0.001 - start / 1000))


        val loadNGrams = System.currentTimeMillis()
        val pmi = PMI("/home/padre/arxiv/arxiv.unigram", "/home/padre/arxiv/arxiv.bigram.fltr10000.gz", alphabet, 10, Category, " ")
        println(" loadNGrams " + (System.currentTimeMillis() * 0.001 - loadNGrams / 1000))

        val calculatePMI = System.currentTimeMillis()
        println("pmi " +  pmi.meanPMI(trainedModel.phi(Category)).map(_._2).sum)
        println(" calculatePMI time " + (System.currentTimeMillis() * 0.001 - calculatePMI / 1000))
    }

    doIt(0f)

}
