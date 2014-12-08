package ru.ispras.modis.tm.scripts

import java.io.File
import java.util.Random

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.builder.LDABuilder
import ru.ispras.modis.tm.documents.{Numerator, TextualDocument}
import ru.ispras.modis.tm.utils.ElapsedTime

import scala.io.Source

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object NotSoBigData extends App with  ElapsedTime {

    def getDocs(path: String, beta: Float) = {
        val lines = Source.fromFile(new File(path)).getLines().take(30000).map(line => new TextualDocument(Map(DefaultAttributeType -> line.split("\\s+"))))
        val random = new Random(13)
        val (docs, alphabet) = Numerator(lines, 5)

        val plsa = new LDABuilder(25,
            alphabet,
            docs,
            0f,
            beta,
            random,
            30).build()


        (plsa, docs, alphabet)
    }

    def doIt(param: Float) {
        val (plsa, docs, alphabet) = time("reading documents") {
            getDocs("examples/arxiv.part", param)
        }

        info("number of words " + alphabet.numberOfWords()(DefaultAttributeType))
        val trainedModel = time("plsa training ") {
            plsa.train
        }

        println(trainedModel)
        //        TopicHelper.saveMatrix("/home/padre/tmp/Theta" + param, trainedModel.theta)
        //        TopicHelper.printAllTopics(10, trainedModel.phi(Category), alphabet)

        //        val fixedPhi = new FixedPhiBuilder(alphabet, docs, 100, trainedModel.phi).build()
        //        val fixedTheta = fixedPhi.train(docs).theta
        //        TopicHelper.saveMatrix("/home/padre/tmp/Theta_fixed" + param, fixedTheta)
        //
        //        val loadNGrams = System.currentTimeMillis()
        //        val pmi = PMI("/home/padre/arxiv/arxiv.unigram", "/home/padre/arxiv/arxiv.bigram.fltr10000.gz", alphabet, 10, Category, " ")
        //        println(" loadNGrams " + (System.currentTimeMillis() * 0.001 - loadNGrams / 1000))
        //
        //        val calculatePMI = System.currentTimeMillis()
        //        println("pmi " +  pmi.meanPMI(trainedModel.phi(Category)).map(_._2).sum)
        //        println(" calculatePMI time " + (System.currentTimeMillis() * 0.001 - calculatePMI / 1000))
    }

    doIt(0.0f)


}
