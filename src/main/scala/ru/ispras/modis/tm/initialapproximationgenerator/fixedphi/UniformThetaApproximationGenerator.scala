package ru.ispras.modis.tm.initialapproximationgenerator.fixedphi

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.Ogre._
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 07.05.14
 * Time: 15:00
 */
class UniformThetaApproximationGenerator(phi: Map[AttributeType, AttributedPhi]) extends FixedPhiInitialApproximation(phi) {
    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before return matrices
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     */
    protected def fillMatrixTheta(parameters: ModelParameters, documents: Array[Document], theta: Theta): Unit =
        theta.addToExpectation(1f)
}
