package ru.ispras.modis.tm.regularizer

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.matrix.{ImmutablePhi, ImmutableTheta, AttributedPhi, Theta}
import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 20:59
 */
/**
 * this class implements a symmetric dirichlet regularizer. Symmetric dirichlet regularizer is convert PLSA into LDA, it
 * can make topic more sparse or more smooth.
 * @param alpha parameter for phi (words by topic distribution). Dirichlet distribution is defined for alpha > -1. You may
 *              use alpha <= -1, but do it at yours own risk
 * @param beta parameter for theta (document by topic distribution). Dirichlet distribution is defined for beta > -1. You may
 *              use alpha <= -1, but do it at yours own risk
 */
class SymmetricDirichlet(private val alpha: Float, private val beta: Float) extends Regularizer with Logging{
    if (alpha <= -1) {warn("Dirichlet distribution define only for alpha > -1 but alpha = " + alpha + " Use this alpha at yours own risk ")}
    if (beta <= -1) {warn("Dirichlet distribution define only for beta > -1 but beta = " + beta + " Use this beta at yours own risk ")}

    /**
     * we return zero to be able to compare results with standard PLSA. In the case of using this regularizer perplexity
     * may grows in some iteration, this is not a mistake.
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     * @return log(P(Phi, Theta| prior))
     */
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float = 0f

    /**
     * To regularize matrix Phi one should add phi(w, t) * (dR(Phi, Theta) / d phi(w, t)) to every cells w, t.
     * R denote current regularizer, dR(Phi, Theta) / d phi(w, t) is a partial derivative, w - word number,
     * t - topic number. We describe how to calculate dR(Phi, Theta) / d theta(d, t) in user guide,
     * dR(Phi, Theta) / d phi(w, t) may be calculated analogously.
     * To apply regularizer we go through the rows (0 until phi.numberOfRows).foreach(row =>
     * and columns (0 until phi.numberOfColumns).foreach(column =>
     * and add alpha to every cell phi.addToExpectation(row, column, alpha)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics. Theta is immutable, so you can't add to it
     */
    def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) {
        (0 until phi.numberOfRows).foreach(row => (0 until phi.numberOfColumns).foreach(column => phi.addToExpectation(row, column, alpha)))
    }

    /**
     * To regularize matrix Phi one should add theta(d, t) * (dR(Phi, Theta) / d theta(d, t)) to every cells d, t.
     * R denote current regularizer, dR(Phi, Theta) / d phi(w, t) is a partial derivative, d - document number,
     * t - topic number. We describe how to calculate dR(Phi, Theta) / d theta(d, t) in user guide
     * To apply regularizer we go through the rows (0 until theta.numberOfRows).foreach(row =>
     * and columns (0 until theta.numberOfColumns).foreach(column =>
     * and add alpha to every cell theta.addToExpectation(row, column, alpha)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) = {
        (0 until theta.numberOfRows).foreach(row => (0 until theta.numberOfColumns).foreach(column => theta.addToExpectation(row, column, beta)))
    }
}

