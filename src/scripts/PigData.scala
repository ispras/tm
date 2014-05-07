package scripts

import documents.{TextualDocument, Numerator}
import java.util.Random
import scala.io.Source
import java.io.{FileWriter, File}
import attribute.Category
import builder.{FixedPhiBuilder, LDABuilder, PLSABuilder, RobustPLSABuilder}
import qualitimeasurment.{PMI}
import utils.{ModelParameters, TopicProcessing}
import brick.fixedphi.NonRobustPhiFixedBrick

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
            0.05f,
            param,
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
        TopicProcessing.saveMatrix("/home/padre/tmp/Theta" + param, trainedModel.theta)
        TopicProcessing.printAllTopics(10, trainedModel.phi(Category), alphabet)
        println("train time " + (System.currentTimeMillis() * 0.001 - start / 1000))

        val fixedPhi = new FixedPhiBuilder(alphabet, docs, 100, trainedModel.phi).build()
        val fixedTheta = fixedPhi.train(docs).theta
        TopicProcessing.saveMatrix("/home/padre/tmp/Theta_fixed" + param, fixedTheta)

        val loadNGrams = System.currentTimeMillis()
        val pmi = PMI("/home/padre/arxiv/arxiv.unigram", "/home/padre/arxiv/arxiv.bigram.fltr10000.gz", alphabet, 10, Category, " ")
        println(" loadNGrams " + (System.currentTimeMillis() * 0.001 - loadNGrams / 1000))

        val calculatePMI = System.currentTimeMillis()
        println("pmi " +  pmi.meanPMI(trainedModel.phi(Category)).map(_._2).sum)
        println(" calculatePMI time " + (System.currentTimeMillis() * 0.001 - calculatePMI / 1000))
    }

    doIt(0f)



}
