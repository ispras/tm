package ru.ispras.modis.builder

import ru.ispras.modis.documents.{Alphabet, Document}
import ru.ispras.modis.utils.ModelParameters
import ru.ispras.modis.brick.{AbstractPLSABrick}
import ru.ispras.modis.brick.fixedphi.NonRobustPhiFixedBrick
import ru.ispras.modis.matrix.AttributedPhi
import ru.ispras.modis.initialapproximationgenerator.fixedphi.UniformThetaApproximationGenerator
import ru.ispras.modis.stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import ru.ispras.modis.attribute.AttributeType

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
