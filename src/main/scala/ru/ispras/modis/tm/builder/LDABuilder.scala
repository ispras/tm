package ru.ispras.modis.tm.builder

import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.documents.Alphabet
import java.util.Random
import ru.ispras.modis.tm.regularizer.{Regularizer, SymmetricDirichlet}
import ru.ispras.modis.tm.brick.{NonRobustBrick, AbstractPLSABrick}
import ru.ispras.modis.tm.sparsifier.{ZeroSparsifier, Sparsifier}
import ru.ispras.modis.tm.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.tm.initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import ru.ispras.modis.tm.attribute.AttributeType

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
