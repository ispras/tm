package plsa

import matrix.AttributedPhi
import sparsifier.Sparsifier
import regularizer.Regularizer
import brick.AbstractPLSABrick

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:02
 */
trait AbstractPLSABrickFactory {
    def apply(initialPhi: AttributedPhi, phiSparsifier: Sparsifier, regularizer: Regularizer): AbstractPLSABrick
}
