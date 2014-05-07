package builder

import documents.{Alphabet, Document}
import utils.ModelParameters
import attribute.AttributeType
import brick.{AbstractPLSABrick}
import brick.fixedphi.NonRobustPhiFixedBrick
import matrix.AttributedPhi
import initialapproximationgenerator.fixedphi.UniformThetaApproximationGenerator
import stoppingcriteria.MaxNumberOfIterationStoppingCriteria

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 06.05.14
 * Time: 15:54
 */
class FixedPhiBuilder(alphabet: Alphabet,
                      documents: Seq[Document],
                      numberOfIterations: Int,
                      private val phi: Map[AttributeType, AttributedPhi])
    extends AbstractPLSABuilder(phi.head._2.numberOfRows, alphabet, documents){

    override protected def buildBricks(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = {
        modelParameters.numberOfWords.map{case(attribute, numberOfWords) =>
            (attribute, new NonRobustPhiFixedBrick(attribute, modelParameters))
        }
    }

    initialApproximationGenerator = new UniformThetaApproximationGenerator(phi)
    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIterations)
}
