package plsa

import attribute.AttributeType
import brick.AbstractPLSABrick
import stoppingcriteria.StoppingCriteria
import sparsifier.Sparsifier
import matrix.{AttributedPhi, Theta}
import documents.Document
import regularizer.Regularizer

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:37
 */
class PLSA(private val bricks: Map[AttributeType, AbstractPLSABrick],
           private val stoppingCriteria: StoppingCriteria,
           private val thetaSparsifier: Sparsifier,
           private val regularizer: Regularizer,
           private val phi: Map[AttributeType, AttributedPhi],
           private val theta: Theta) {

    def train(documents: Seq[Document]): TrainedModel = {
        val collectionLength = documents.foldLeft(0) {
            (sum, document) => sum + document.attributes.values.foldLeft(0)((sumOneDocument, words) => sumOneDocument + words.map(_._2).sum)
        }
        var numberOfIteration = 0
        var oldPpx = 0d
        var newPpx = 0d
        while (!stoppingCriteria(numberOfIteration, oldPpx, newPpx)) {
            oldPpx = newPpx
            newPpx = perplexity(bricks.foldLeft(0d) {
                case (sum, (attribute, brick)) =>
                    sum + brick.makeIteration(theta, phi(attribute), documents, numberOfIteration)
            }, collectionLength)
            println(newPpx) //TODO logg
            applyRegularizer()
            theta.dump()
            numberOfIteration += 1
        }

        new TrainedModel(phi, theta)
    }

    def perplexity(logLikelihood: Double, collectionLength: Int) = math.exp(-logLikelihood / collectionLength)

    private def applyRegularizer() {
        var t = 0
        var d = 0
        while (d < theta.numberOfRows) {
            while (t < theta.numberOfColumns) {
                theta.addToExpectation(d, t, regularizer.derivativeByTheta(d, t, theta, phi))
                t += 1
            }
            t = 0
            d += 1
        }
    }
}
