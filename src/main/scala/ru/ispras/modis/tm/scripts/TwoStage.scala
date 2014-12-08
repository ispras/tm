package ru.ispras.modis.tm.scripts

import java.io.File
import java.util.Random

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.builder.LDABuilder
import ru.ispras.modis.tm.documents._
import ru.ispras.modis.tm.initialapproximationgenerator.fixedphi.OptimizedThetaApproximationGenerator
import ru.ispras.modis.tm.plsa.TrainedModel

import scala.io.Source

/**
 * Created by valerij on 07.12.14.
 */
object TwoStage extends App with Logging {
    val path = "examples/arxiv.part" 
    
    val lines = Source.fromFile(new File(path)).getLines().take(30000).map(_.split(" ").toSeq)
    val random = new Random(13)
    val (docs, alphabet) = SingleAttributeNumerator(lines)

    val docsSample = new DatasetSampler(random).apply(docs, 0.5)

    val plsaSub = new LDABuilder(25,
        alphabet,
        docsSample,
        0.3f,
        0.2f,
        random,
        30).build()

    val trainedSubModel: TrainedModel = plsaSub.train
    info("submodel trained")

    val initialApproximation = new OptimizedThetaApproximationGenerator(alphabet, trainedSubModel)

    val plsa = new LDABuilder(25,
        alphabet,
        docs,
        0.01f,
        0.01f,
        random,
        30).setInitialApproximationGenerator(initialApproximation).build()

    info("big model is ready")

    plsa.train

    info("big model is trained")
    
}
