package brick

import regularizer.Regularizer
import sparsifier.Sparsifier
import documents.Document
import attribute.AttributeType
import matrix.{AttributedPhi, Theta}
import utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 20:31
 */
abstract class AbstractPLSABrick(private val regularizer: Regularizer,
                                 private val phiSparsifier: Sparsifier,
                                 private val attribute: AttributeType,
                                 private val modelParameters: ModelParameters) {


    def makeIteration(theta: Theta, phi: AttributedPhi, documents: Seq[Document], iterationCnt: Int): Float

    protected def applyRegularizer(theta: Theta, phi: AttributedPhi) {
        var w = 0
        var t = 0
        while (t < phi.numberOfRows) {
            while (w < phi.numberOfColumns) {
                phi.addToExpectation(t, w, regularizer.derivativeByPhi(attribute)(t: Int, w: Int, theta: Theta, phi: AttributedPhi))
                w += 1
            }
            w = 0
            t += 1
        }
    }
}
