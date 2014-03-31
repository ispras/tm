package stoppingcriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:52
 */
class MaxNumberOfIteration(private val maxNumberOfIteration: Int) extends StoppingCriteria {
    def apply(numberOfIteration: Int, oldPerplexity: Float, newPerplexity: Float): Boolean = numberOfIteration >= maxNumberOfIteration
}
