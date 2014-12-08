package ru.ispras.modis.tm.builder

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.brick.{AbstractPLSABrick, NonRobustBrick}
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.initialapproximationgenerator.RandomInitialApproximationGenerator
import ru.ispras.modis.tm.regularizer.ZeroRegularizer
import ru.ispras.modis.tm.sparsifier.ZeroSparsifier
import ru.ispras.modis.tm.stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 18:54
 */
class PLSABuilder(numberOfTopics: Int,
                  alphabet: Alphabet,
                  documents: Array[Document],
                  protected val random: Random = new Random,
                  protected val numberOfIteration: Int = 100,
                  attributeWeight: Map[AttributeType, Float] = Map[AttributeType, Float](),
                  parallel : Boolean = false)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents, attributeWeight, parallel) {

    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO user setters

    thetaSparsifier = new ZeroSparsifier()

    phiSparsifier = new ZeroSparsifier()

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration)

    regularizer = new ZeroRegularizer()

}
