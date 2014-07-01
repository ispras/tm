package ru.ispras.modis.tm.builder

import ru.ispras.modis.tm.documents.{Document, Alphabet}
import ru.ispras.modis.tm.initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import java.util.Random
import ru.ispras.modis.tm.utils.ModelParameters
import ru.ispras.modis.tm.brick.{NonRobustBrick, AbstractPLSABrick}
import ru.ispras.modis.tm.sparsifier.{ZeroSparsifier, Sparsifier}
import ru.ispras.modis.tm.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.tm.regularizer.{ZeroRegularizer, Regularizer}
import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 18:54
 */
class PLSABuilder(numberOfTopics: Int,
                  alphabet: Alphabet,
                  documents: Seq[Document],
                  protected val random: Random = new Random,
                  protected val numberOfIteration: Int = 100)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents) {

    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO user setters

    thetaSparsifier = new ZeroSparsifier()

    phiSparsifier = new ZeroSparsifier()

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration)

    regularizer = new ZeroRegularizer()

    protected def brickBuilder(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = modelParameters.numberOfWords.map {
        case (key, value) => (key, new NonRobustBrick(regularizer, phiSparsifier, key, modelParameters))
    }
}
