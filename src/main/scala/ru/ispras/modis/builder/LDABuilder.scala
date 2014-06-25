package ru.ispras.modis.builder

import ru.ispras.modis.documents.Document
import ru.ispras.modis.documents.Alphabet
import java.util.Random
import ru.ispras.modis.regularizer.{Regularizer, SymmetricDirichlet}
import ru.ispras.modis.brick.{NonRobustBrick, AbstractPLSABrick}
import ru.ispras.modis.sparsifier.{ZeroSparsifier, Sparsifier}
import ru.ispras.modis.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import ru.ispras.modis.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 17:51
 */
class LDABuilder(numberOfTopics: Int,
                 alphabet: Alphabet,
                 documents: Seq[Document],
                 private val alpha: Float,
                 private val beta: Float,
                 private val random: Random = new Random(),
                 private val maxNumberOfIteration: Int = 100) extends AbstractPLSABuilder(numberOfTopics, alphabet, documents) {
    require(alpha > -1)
    require(beta > -1)
    initialApproximationGenerator = new RandomInitialApproximationGenerator(random) //TODO use setters

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(maxNumberOfIteration)
    regularizer = new SymmetricDirichlet(alpha: Float, beta: Float)
}
