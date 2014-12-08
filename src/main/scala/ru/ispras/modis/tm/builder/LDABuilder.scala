package ru.ispras.modis.tm.builder

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.initialapproximationgenerator.RandomInitialApproximationGenerator
import ru.ispras.modis.tm.regularizer.SymmetricDirichlet
import ru.ispras.modis.tm.stoppingcriteria.MaxNumberOfIterationStoppingCriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 17:51
 */
class LDABuilder(numberOfTopics: Int,
                 alphabet: Alphabet,
                 documents: Array[Document],
                 private val alpha: Float,
                 private val beta: Float,
                 private val random: Random = new Random(),
                 private val maxNumberOfIteration: Int = 100,
                 attributeWeight: Map[AttributeType, Float] = Map[AttributeType, Float](),
                    parallel : Boolean = false)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents, attributeWeight,parallel ) {



    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO use setters

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(maxNumberOfIteration)
    regularizer = new SymmetricDirichlet(alpha: Float, beta: Float)
}
