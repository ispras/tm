package ru.ispras.modis.tm.stoppingcriteria

/**
 * Created by valerij on 12/4/14.
 */
class PerplexityConvergenceStoppingCriteria(private val relativeDecrease : Double = 0.001) extends StoppingCriteria {
    /**
     * @param numberOfIteration the number of iteration that has been already done.
     * @param oldPerplexity perplexity after the previous iteration
     * @param newPerplexity perplexity after this iteration
     * @return true if it is time to stop, false otherwise
     */
    override def apply(numberOfIteration: Int, oldPerplexity: Double, newPerplexity: Double): Boolean =
        (oldPerplexity - newPerplexity) / newPerplexity < relativeDecrease
}
