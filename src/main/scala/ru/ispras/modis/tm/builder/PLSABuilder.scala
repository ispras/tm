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
                  protected val numberOfIteration: Int = 100,
                  attributeWeight: Map[AttributeType, Float] = Map[AttributeType, Float]())
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents, attributeWeight) {

    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO user setters

    thetaSparsifier = new ZeroSparsifier()

    phiSparsifier = new ZeroSparsifier()

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration)

    regularizer = new ZeroRegularizer()

    protected def brickBuilder(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = modelParameters.numberOfWords.map {
        case (attribute, value) => (attribute,
            new NonRobustBrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight.getOrElse(attribute, 1f)))
    }
}
