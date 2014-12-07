package ru.ispras.modis.tm.regularizer

import grizzled.slf4j.Logging
import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}
import ru.ispras.modis.tm.matrix.Ogre._
import ru.ispras.modis.tm.matrix.{AttributedPhi, ImmutablePhi, ImmutableTheta, Theta}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 03.04.14
 * Time: 20:59
 */
/**
 * this class implements a symmetric dirichlet regularizer. Symmetric dirichlet regularizer is convert PLSA into LDA, it
 * can make topic more sparse or more smooth.
 * @param alphaForPhi Dirichlet distribution parameters for phi (words by topic distribution). Parameters are indexed with AttributeType they are related to.
 *                    Dirichlet distribution is defined for alphaForPhi > -1. You may use alphaForPhi <= -1, but do it at your own risk
 * @param alphaForTheta Dirichlet distribution parameter for theta (document by topic distribution). Dirichlet distribution is defined for alphaForTheta > -1. You may
 *                      use alphaForPhi <= -1, but do it at yours own risk
 */
class SymmetricDirichlet(private val alphaForPhi: Map[AttributeType, Float], private val alphaForTheta: Float) extends Regularizer with Logging {
    /**
     * if you have only a single attribute and have no desire to think about AttributeTypes, you can use this constructor
     *
     * Note, DefaultAttributeType will be implicitly substituted
     * @param alphaForPhi -- Dirichlet distribution parameters for phi matrix
     *                    Dirichlet distribution is defined for alphaForPhi > -1. You may use alphaForPhi <= -1, but do it at your own risk
     * @param alphaForTheta Dirichlet distribution parameters for theta matrix
     *                      Dirichlet distribution is defined for alphaForPhi > -1. You may use alphaForPhi <= -1, but do it at your own risk
     * @return
     */
    def this(alphaForPhi: Float, alphaForTheta: Float) = this(Map[AttributeType, Float](DefaultAttributeType -> alphaForPhi), alphaForTheta)

    if (!alphaForPhi.forall(_._2 > -1)) {
        warn("Dirichlet distribution is defined only for alphaForPhi > -1 but alphaForPhi = " + alphaForPhi + " Use this alphaForPhi at your own risk ")
    }
    if (alphaForTheta <= -1) {
        warn("Dirichlet distribution is defined only for alphaForTheta > -1 but alphaForTheta = " + alphaForTheta + " Use this alphaForTheta at your own risk ")
    }

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
     * and add alphaForPhi to every cell phi.addToExpectation(row, column, alphaForPhi)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics. Theta is immutable, so you can't add to it
     */
    def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta) = phi.addToExpectation(alphaForPhi(phi.attribute))

    /**
     * To regularize matrix Phi one should add theta(d, t) * (dR(Phi, Theta) / d theta(d, t)) to every cells d, t.
     * R denote current regularizer, dR(Phi, Theta) / d phi(w, t) is a partial derivative, d - document number,
     * t - topic number. We describe how to calculate dR(Phi, Theta) / d theta(d, t) in user guide
     * To apply regularizer we go through the rows (0 until theta.numberOfRows).foreach(row =>
     * and columns (0 until theta.numberOfColumns).foreach(column =>
     * and add alphaForPhi to every cell theta.addToExpectation(row, column, alphaForPhi)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta) = theta.addToExpectation(alphaForTheta)
}

