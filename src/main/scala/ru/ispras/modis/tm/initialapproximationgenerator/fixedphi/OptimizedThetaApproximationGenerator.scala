package ru.ispras.modis.tm.initialapproximationgenerator.fixedphi

import ru.ispras.modis.tm.builder.FixedPhiBuilder
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.matrix.Theta
import ru.ispras.modis.tm.plsa.TrainedModel
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created by valerij on 07.12.14.
 */
class OptimizedThetaApproximationGenerator(private val alphabet : Alphabet,
                                           private val trainedModel : TrainedModel) extends FixedPhiInitialApproximation(trainedModel.phi) {
    
    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before matrices are returned
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     */
    override protected def fillMatrixTheta(parameters: ModelParameters, documents: Array[Document], theta: Theta): Unit = {
        val fixedPLSA = new FixedPhiBuilder(alphabet, documents, 5, trainedModel.phi, trainedModel.attributeWeight).build()

        val obtainedTheta = fixedPLSA.train.theta

        theta.addToExpectation((i,j) => obtainedTheta.probability(i,j))
    }
}
