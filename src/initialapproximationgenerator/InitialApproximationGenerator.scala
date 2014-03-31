package initialapproximationgenerator

import documents.Document
import matrix.{AttributedPhi, Theta}
import utils.ModelParameters
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 18:39
 */
trait InitialApproximationGenerator {
    def apply(parameters: ModelParameters, documents: Seq[Document]): (Theta, Map[AttributeType, AttributedPhi])
}
