package ru.ispras.modis.tm.builder

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.brick.{NonRobustPreciseThetaPLSABrick, AbstractPLSABrick, NonRobustBrick}
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.initialapproximationgenerator.{InitialApproximationGenerator, RandomInitialApproximationGenerator}
import ru.ispras.modis.tm.plsa.PLSA
import ru.ispras.modis.tm.regularizer.RegularizerSum.toRegularizerSum
import ru.ispras.modis.tm.regularizer.{Regularizer, ZeroRegularizer}
import ru.ispras.modis.tm.sparsifier.{Sparsifier, ZeroSparsifier}
import ru.ispras.modis.tm.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.tm.utils.ModelParameters


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 17:45
 */
/**
 * this class can build PLSA!
 */
abstract class AbstractPLSABuilder(protected val numberOfTopics: Int,
                                   protected val alphabet: Alphabet,
                                   protected val documents: Array[Document],
                                   protected val attributeWeight: Map[AttributeType, Float],
                                   private val parallel : Boolean,
                                   private val numberOfThetaIterations : Map[AttributeType, Int] = Map[AttributeType, Int]()) {

    protected val modelParameters = new ModelParameters(numberOfTopics, alphabet.numberOfWords())

    protected var initialApproximationGenerator: InitialApproximationGenerator = new RandomInitialApproximationGenerator(new Random)

    def setInitialApproximationGenerator(newValue: InitialApproximationGenerator) = {
        initialApproximationGenerator = newValue
        this
    }


    protected var stoppingCriteria: StoppingCriteria = new MaxNumberOfIterationStoppingCriteria(100)

    def setStoppingCriteria(newValue: StoppingCriteria) = {
        stoppingCriteria = newValue
        this
    }

    protected var thetaSparsifier: Sparsifier = new ZeroSparsifier()

    def setThetaSparsifier(newValue: Sparsifier) = {
        thetaSparsifier = newValue
        this
    }

    protected var phiSparsifier: Sparsifier = new ZeroSparsifier()

    def setPhiSparsifier(newValue: Sparsifier) = {
        phiSparsifier = newValue
        this
    }

    protected var regularizer: Regularizer = new ZeroRegularizer()

    def setRegularizer(newValue: Regularizer) = {
        regularizer = newValue
        this
    }

    def addRegularizer(newValue: Regularizer) = {
        regularizer += newValue
        this
    }


    protected def buildBricks(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = {
        modelParameters.numberOfWords.map {
            case (attribute, numberOfWords) => (attribute, buildBrick(modelParameters, attribute,numberOfWords ))
        }
    }

    private def buildBrick(modelParameters: ModelParameters, attribute : AttributeType, numberOfWords : Int) = {
        val thetaIter: Int = numberOfThetaIterations.getOrElse(attribute, 1)
        if (thetaIter == 1)
            new NonRobustBrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight.getOrElse(attribute, 1f), parallel)
        else
            new NonRobustPreciseThetaPLSABrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight.getOrElse(attribute, 1f), thetaIter, parallel)
    }

    def build(): PLSA = {
        val (theta, phi) = initialApproximationGenerator(modelParameters, documents)
        val bricks = buildBricks(modelParameters: ModelParameters)
        new PLSA(bricks, stoppingCriteria, thetaSparsifier, regularizer, documents, phi, theta)
    }
}



