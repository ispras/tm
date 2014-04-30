package scripts

import documents.{TextualDocument, Numerator}
import java.util.Random
import scala.io.Source
import java.io.{FileWriter, File}
import attribute.Category
import builder.{LDABuilder, PLSABuilder, RobustPLSABuilder}
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

        val lines = Source.fromFile(new File(path)).getLines().take(3000).map(line => new TextualDocument(Map(Category -> line.split(" "))))
        val random = new Random
        random.setSeed(13)
        val (docs, alphabet) = Numerator(lines)

        val plsa = new LDABuilder(25,
            alphabet,
            docs,
            1e-8f,
            0.1e-5f,
            random,
            100).build()



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
        val out = new FileWriter("/home/padre/tmp/Phi")
        val phi = 0.until(trainedModel.phi(Category).numberOfRows).map{topicId =>
            0.until(trainedModel.phi(Category).numberOfColumns).map(wordId => trainedModel.phi(Category).probability(topicId, wordId)).mkString(", ")
        }.mkString("\n")
        out.write(phi)
        out.close()

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
