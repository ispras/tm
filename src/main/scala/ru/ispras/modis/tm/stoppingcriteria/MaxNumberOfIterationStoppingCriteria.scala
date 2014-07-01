package ru.ispras.modis.tm.stoppingcriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:52
 */
/**
 * this stopping criterion execute a fixed number of iteration and stop
 * @param maxNumberOfIteration number of iteration to stop
 */
class MaxNumberOfIterationStoppingCriteria(private val maxNumberOfIteration: Int) extends StoppingCriteria {
    /**
     *
     * @param numberOfIteration the number of iteration that has been already done.
     * @param oldPerplexity perplexity after the previous iteration.
     * @param newPerplexity perplexity after this iteration
     * @return true if iterationCnt >= maxNumberOfIteration
     */
    def apply(numberOfIteration: Int, oldPerplexity: Double, newPerplexity: Double): Boolean = numberOfIteration >= maxNumberOfIteration
}
