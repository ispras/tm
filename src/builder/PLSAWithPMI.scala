package builder

import java.util.Random
import documents.Document
import documents.Alphabet
import regularizer.{PMIRegularizer, Regularizer}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 09.04.14
 * Time: 18:57
 */
class PLSAWithPMI(numberOfTopics: Int,
                  alphabet: Alphabet,
                  documents: Seq[Document],
                  random: Random,
                  numberOfIteration: Int,
                  private val pathToUnigrams: String,
                  private val pathToBigrams: String,
                  private val parameter: Float,
                  private val n: Int,
                  private val attribute: AttributeType,
                  private val sep: String) extends PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration){
    override protected def regularizer: Regularizer = PMIRegularizer(pathToUnigrams, pathToBigrams, alphabet, parameter, n, attribute, sep )
}
