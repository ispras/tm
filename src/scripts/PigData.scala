package scripts

import documents.{TextualDocument, Numerator}
import scala.reflect.io.File
import attribute.Category
import plsa.RobustPLSAFactory
import initialapproximationgenerator.RandomInitialApproximationGenerator
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import stoppingcriteria.MaxNumberOfIteration
import java.util.Random
import brick.NoiseParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App {
    def getDocs(path: String) = {
        val lines = File(path).lines().map(line => new TextualDocument(Map(Category -> line.split(" "))))
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
            new MaxNumberOfIteration(33),
            alphabet,
            noiseParameter)
        (plsa, docs)
    }


    val (plsa, docs) = getDocs("/media/3d6a5a46-cbd3-49cd-abd4-2907eed0831a/home/padre/data/arxiv/part")
    val start = System.currentTimeMillis()
    val trainedModel = plsa.train(docs)
    println(trainedModel.theta)
    println(" time " + (System.currentTimeMillis() * 0.001 - start / 1000))
}
