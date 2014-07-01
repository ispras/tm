package ru.ispras.modis.tm.stoppingcriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 17:49
 */
/**
 * PLSA use this class to check is it time to stop EM-algorithm and return result
 */
trait StoppingCriteria {
    /**
     * @param numberOfIteration the number of iteration that has been already done.
     * @param oldPerplexity perplexity after the previous iteration
     * @param newPerplexity perplexity after this iteration
     * @return true if it time to stop, false otherwise
     */
    def apply(numberOfIteration: Int, oldPerplexity: Double, newPerplexity: Double): Boolean
}
