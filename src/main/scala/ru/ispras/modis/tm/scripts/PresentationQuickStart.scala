package ru.ispras.modis.tm.scripts

import java.io.File
import java.util.Random

import ru.ispras.modis.tm.builder.{FixedPhiBuilder, PLSABuilder}
import ru.ispras.modis.tm.documents.SingleAttributeNumerator
import ru.ispras.modis.tm.initialapproximationgenerator.GibbsInitialApproximationGenerator
import ru.ispras.modis.tm.plsa.TrainedModelSerializer
import ru.ispras.modis.tm.regularizer.SymmetricDirichlet
import ru.ispras.modis.tm.sparsifier.{ThresholdSparsifier, ZeroSparsifier}
import ru.ispras.modis.tm.utils.TopicHelper

import scala.io.Source

/**
 * Created by valerij on 7/2/14.
 */
object PresentationQuickStart extends App {
    def getTokenizedDocuments(): Iterator[Seq[String]] = {
        /**
         * read lines from textual file with a 1000 scientific articles
         */
        val lines = Source.fromFile(new File("examples/arxiv.part")).getLines()

        /**
         * split each line by space
         */
        lines.map(_.split(" ").toSeq)
    }

    val textualDocuments = getTokenizedDocuments()

    val (documents, alphabet) = SingleAttributeNumerator(textualDocuments)

    val numberOfTopics = 25
    val numberOfIteration = 100
    val random = new Random()
    val plsa = new PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration).build()

    val trainedModel = plsa.train // the deepest and the darkest magic

    val phiArrayArray = TopicHelper.copyMatrixToArray(trainedModel.getPhi)

    TrainedModelSerializer.save(trainedModel, "examples/model")

    TrainedModelSerializer.load("examples/model")

    val fixedPhi = new FixedPhiBuilder(alphabet, documents, numberOfIteration, trainedModel.phi).build()

    val newTrainedModel = fixedPhi.train

}


object BuilderConfugration extends App {
    def getTokenizedDocuments(): Iterator[Seq[String]] = {
        /**
         * read lines from textual file with a 1000 scientific articles
         */
        val lines = Source.fromFile(new File("examples/arxiv.part")).getLines()

        /**
         * split each line by space
         */
        lines.map(_.split(" ").toSeq)
    }

    val textualDocuments = getTokenizedDocuments()

    val (documents, alphabet) = SingleAttributeNumerator(textualDocuments)

    val numberOfTopics = 25
    val numberOfIteration = 100
    val random = new Random()

    val plsa = new PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration)
        .addRegularizer(new SymmetricDirichlet(0.1f, 0.1f))
        .setInitialApproximationGenerator(new GibbsInitialApproximationGenerator(random))
        .setThetaSparsifier(new ThresholdSparsifier(0.01f, 10, Integer.MAX_VALUE))
        .setPhiSparsifier(new ZeroSparsifier)
        .build()

    val trainedModel = plsa.train // the deepest and the darkest magic

    val phiArrayArray = TopicHelper.copyMatrixToArray(trainedModel.getPhi)

    TrainedModelSerializer.save(trainedModel, "examples/model")

    TrainedModelSerializer.load("examples/model")

}