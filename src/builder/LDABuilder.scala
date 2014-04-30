package builder

import documents.Document
import documents.Alphabet
import java.util.Random
import regularizer.{Regularizer, SymmetricDirichlet}
import attribute.AttributeType
import brick.{NonRobustBrick, AbstractPLSABrick}
import sparsifier.{ZeroSparsifier, Sparsifier}
import stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}

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
                 private val maxNumberOfIteration: Int = 100)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents)
{
    initialApproximationGenerator = new RandomInitialApproximationGenerator(random)

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(maxNumberOfIteration)
    regularizer = new SymmetricDirichlet(alpha: Float, beta: Float)
}
