package ru.ispras.modis.tm.scripts

import java.util.Random

import ru.ispras.modis.tm.attribute.Word
import ru.ispras.modis.tm.builder.PLSABuilder
import ru.ispras.modis.tm.documents.{Numerator, TextualDocument}
import ru.ispras.modis.tm.plsa.TrainedModelSerializer
import ru.ispras.modis.tm.utils.TopicHelper

/**
 * Created by padre on 03.07.14.
 */
object MultilingualQuickStart extends App {

    val td1 = new TextualDocument(Map(
        Word("ru") -> Seq("утки", "утки", "утки", "жопа"),
        Word("en") -> Seq("duck", "duck", "duck", "duck", "duck", "fuckaduck")))

    val td2 = new TextualDocument(Map(
        Word("ru") -> Seq("утки", "утки", "интеграл"),
        Word("en") -> Seq("duck", "duck", "duck", "integral", "gradient")))

    val td3 = new TextualDocument(Map(
        Word("ru") -> Seq("градиент", "производная", "интеграл"),
        Word("en") -> Seq("integral", "logarithm", "integral", "integral", "gradient")))

    val td4 = new TextualDocument(Map(Word("ru") -> Seq("градиент", "производная", "интеграл", "логарифм")))
    val textualDocuments = Iterator(td1, td2, td3, td4)

    val (documents, alphabet) = Numerator(textualDocuments, 0)


    val numberOfTopics = 2
    val numberOfIteration = 100
    val random = new Random()
    val plsa = new PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration).build()

    val trainedModel = plsa.train
    // the deepest and the darkest magic
    val phiArrayArray = TopicHelper.copyMatrixToArray(trainedModel.getPhi(Word("en")))

    val thetaArrayArray = TopicHelper.copyMatrixToArray(trainedModel.theta)

    println(thetaArrayArray.map(_.mkString(" ")).mkString("\n"))

    println("phi " + trainedModel.getPhi(Word("en")))

    TrainedModelSerializer.save(trainedModel, "examples/model")

    TrainedModelSerializer.load("examples/model")
}
