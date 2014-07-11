package ru.ispras.modis.tm.regularizer

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.matrix.{AttributedPhi, ImmutablePhi, ImmutableTheta, Theta}


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 15:30
 */
/**
 * case L = log(P(D| Theta, Phi)) is non-convex problem the solution of EM-algorithm may converge to a different local
 * maximum. One way to solve this problem is to use regularizer and maximize L + R -> max. Here L is a log likelihood,
 * R - regularizer. For more details see userGuide, section Introduction.
 */
// TODO add description of regularizer as a prior distribution of Phi, Theta
abstract class Regularizer {

    /**
     * calculate R(phi, theta), it is useful for log likelihood calculation.
     * if you want to calculate log(P(D| Phi, Theta)) return 0f
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     * @return log(P(Phi, Theta| prior))
     */
    def apply(phi: Map[AttributeType, AttributedPhi], theta: Theta): Float

    /**
     * apply regularizer to matrix Phi(words by topics). Matrix Theta(document by topics) stay immutable
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    final def regularizePhi(phi: AttributedPhi, theta: Theta): Unit = {
        regularizePhiImmutable(phi, ImmutableTheta.toImmutableTheta(theta))
    }

    /**
     * apply regularizer to given matrix Theta, matrices Phi stay immutable
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    final def regularizeTheta(phi: Map[AttributeType, AttributedPhi], theta: Theta): Unit = {
        regularizeThetaImmutable(phi.map { case (attr, phi) => (attr, ImmutablePhi.toImmutablePhi(phi))}, theta)
    }

    /**
     * this method take into input matrix Phi and matrix Theta and regularize matrix Phi. To regularize matrix Phi one
     * should add phi(w, t) * (dR(Phi, Theta) / d phi(w, t)) to every cells w, t. R denote current regularizer,
     * dR(Phi, Theta) / d phi(w, t) is a partial derivative, w - word number, t - topic number
     * For more detail see userGuide (section introduction)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    private[regularizer] def regularizePhiImmutable(phi: AttributedPhi, theta: ImmutableTheta): Unit

    /**
     * this method take into input matrix Phi and matrix Theta and regularize matrix Phi. To regularize matrix Theta one
     * should add theta(d, t) * (dR(Phi, Theta) / d theta(d, t)) to every cells d, t. R denote current regularizer,
     * dR(Phi, Theta) / d theta(d, t)) is a partial derivative, d - document number, t - topic number
     * For more detail see userGuide (section introduction)
     * @param phi distribution of words by topics
     * @param theta distribution of document by topics
     */
    private[regularizer] def regularizeThetaImmutable(phi: Map[AttributeType, ImmutablePhi], theta: Theta): Unit
}
