package ru.ispras.modis.tm.scripts

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.documents.{TextualDocument, Numerator}
import java.util.Random
import ru.ispras.modis.tm.plsa.TrainedModel

import scala.io.Source
import java.io.{FileWriter, File}
import ru.ispras.modis.tm.builder.{FixedPhiBuilder, LDABuilder, PLSABuilder, RobustPLSABuilder}
import ru.ispras.modis.tm.qualitimeasurment.{PMI}
import ru.ispras.modis.tm.utils.{ModelParameters, TopicHelper}
import ru.ispras.modis.tm.brick.fixedphi.NonRobustPhiFixedBrick
import ru.ispras.modis.tm.attribute.{DefaultAttributeType, Category}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App with Logging {

    def getDocs(path: String, param: Float) = {
        val lines = Source.fromFile(new File(path)).getLines().take(30000).map(line => new TextualDocument(Map(DefaultAttributeType -> line.split(" "))))
        val random = new Random(13)
        val (docs, alphabet) = Numerator(lines)

        val plsa = new LDABuilder(25,
            alphabet,
            docs,
            0.05f,
            param,
            random,
            1).build()


        (plsa, docs, alphabet)
    }

    def doIt(param: Float) {
        val readDataTime = System.currentTimeMillis()
        val (plsa, docs, alphabet) = getDocs("examples/arxiv.part", param)

        println("number of words " + alphabet.numberOfWords()(Category))
        println(" readDataTime " + (System.currentTimeMillis() * 0.001 - readDataTime / 1000))
        val start = System.currentTimeMillis()
        val trainedModel = plsa.train(docs)
        println("training time " + (System.currentTimeMillis() * 0.001 - start / 1000))

        println(trainedModel)
        TopicHelper.saveMatrix("/home/padre/tmp/Theta" + param, trainedModel.theta)
        TopicHelper.printAllTopics(10, trainedModel.phi(Category), alphabet)

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

    doIt(0f)



}
