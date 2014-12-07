package ru.ispras.modis.tm.initialapproximationgenerator.fixedphi

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.builder.FixedPhiBuilder
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.plsa.PLSA
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created by valerij on 07.12.14.
 */
class OptimizedThetaApproximationGenerator(phi: Map[AttributeType, AttributedPhi],
                                           alphabet : Alphabet,
                                           attributeWeight: Map[AttributeType, Float] = Map[AttributeType, Float]()) extends FixedPhiInitialApproximation(phi) {
    
    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before matrices are returned
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     */
    override protected def fillMatrixTheta(parameters: ModelParameters, documents: Array[Document], theta: Theta): Unit = {
        val fixedPLSA = new FixedPhiBuilder(alphabet, documents, 5, phi, attributeWeight).build()

        val obtainedTheta = fixedPLSA.train.theta

        theta.addToExpectation((i,j) => obtainedTheta.probability(i,j))
    }
}
