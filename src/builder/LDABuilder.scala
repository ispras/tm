package builder

import documents.Document
import documents.Alphabet
import java.util.Random
import regularizer.{Regularizer, SymmetricDirichlet}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 17:51
 */
class LDABuilder(numberOfTopics: Int,
                 alphabet: Alphabet,
                 documents: Seq[Document],
                 random: Random,
                 numberOfIteration: Int,
                 private val alpha: Float,
                 private val beta: Float)
    extends PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration)
{
    override protected def regularizer: Regularizer = new SymmetricDirichlet(alpha, beta)
}
