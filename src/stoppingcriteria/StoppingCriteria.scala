package stoppingcriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:49
 */
trait StoppingCriteria {
    def apply(numberOfIteration: Int, oldPerplexity: Double, newPerplexity: Double): Boolean

}
