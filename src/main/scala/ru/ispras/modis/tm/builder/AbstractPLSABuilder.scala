package ru.ispras.modis.tm.builder

import ru.ispras.modis.tm.plsa.PLSA
import ru.ispras.modis.tm.brick.{NonRobustBrick, AbstractPLSABrick}
import ru.ispras.modis.tm.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.tm.sparsifier.{ZeroSparsifier, Sparsifier}
import ru.ispras.modis.tm.regularizer.{ZeroRegularizer, Regularizer}
import ru.ispras.modis.tm.regularizer.Regularizer.toRegularizerSum
import ru.ispras.modis.tm.initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import ru.ispras.modis.tm.documents.{Document, Alphabet}
import ru.ispras.modis.tm.utils.ModelParameters
import java.util.Random
import ru.ispras.modis.tm.attribute.AttributeType


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
                                   protected val documents: Seq[Document]) {

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
            case (attribute, numberOfWords) => (attribute, new NonRobustBrick(regularizer, phiSparsifier, attribute, modelParameters))
        }
    }

    def build(): PLSA = {
        val (theta, phi) = initialApproximationGenerator.apply(modelParameters, documents)
        val bricks = buildBricks(modelParameters: ModelParameters)
        new PLSA(bricks, stoppingCriteria, thetaSparsifier, regularizer, documents, phi, theta)
    }
}



