package scripts

import documents.{TextualDocument, Numerator}
import attribute.Category
import plsa.RobustPLSAFactory
import initialapproximationgenerator.RandomInitialApproximationGenerator
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import java.util.Random
import brick.NoiseParameters
import scala.io.Source
import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App {
    def getDocs(path: String) = {
        val lines = Source.fromFile(new File(path)).getLines().take(100000).map(line => new TextualDocument(Map(Category -> line.split(" "))))
        val random = new Random
        random.setSeed(13)
        val (alphabet, docs) = Numerator(lines.toSeq)
        val noiseParameter = new NoiseParameters(0.00f, 0.0f)
        val plsa = RobustPLSAFactory(new RandomInitialApproximationGenerator(random),
            new ZeroRegularizer(),
            docs,
            10,
            new ZeroSparsifier(),
            new ZeroSparsifier(),
            new MaxNumberOfIterationStoppingCriteria(33),
            alphabet,
            noiseParameter)
        (plsa, docs)
    }


    val (plsa, docs) = getDocs("/mnt/first/pigdata/phis")
    val start = System.currentTimeMillis()
    val trainedModel = plsa.train(docs)
    println(trainedModel)
    println(" time " + (System.currentTimeMillis() * 0.001 - start / 1000))
}
