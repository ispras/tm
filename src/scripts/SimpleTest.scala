package scripts

import documents.{Numerator, TextualDocument}
import attribute.Category
import java.util.Random
import plsa.PLSAFactory
import initialapproximationgenerator.RandomInitialApproximationGenerator
import regularizer.ZeroRegularizer
import sparsifier.ZeroSparsifier
import stoppingcriteria.MaxNumberOfIterationStoppingCriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 15:15
 */
object SimpleTest extends App{
    val td1 = new TextualDocument(Map(Category -> List("ducks", "ducks", "ducks", "ducks")))
    val td2 = new TextualDocument(Map(Category -> List("boobs", "boobs", "boobs", "boobs")))
    val td3 = new TextualDocument(Map(Category -> List("boobs", "ducks", "boobs", "ducks")))
    val td4 = new TextualDocument(Map(Category -> List("boobs", "boobs", "boobs", "ducks")))
    val td5 = new TextualDocument(Map(Category -> List("boobs", "ducks", "ducks", "ducks")))
    val (docs, alphabet) = Numerator.apply(List(td1, td2, td3, td4, td5))
    val random = new Random
    val plsa = PLSAFactory(new RandomInitialApproximationGenerator(random),
        new ZeroRegularizer(),
        docs,
        2,
        new ZeroSparsifier(),
        new ZeroSparsifier(),
        new MaxNumberOfIterationStoppingCriteria(33),
        alphabet)

    val trainedModel = plsa.train(docs)
    println(trainedModel.theta + "\n")
    println(trainedModel.phi(Category))
}
