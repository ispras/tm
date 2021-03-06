package ru.ispras.modis.tm.builder

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.brick.AbstractPLSABrick
import ru.ispras.modis.tm.brick.fixedphi.NonRobustPhiFixedBrick
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.initialapproximationgenerator.fixedphi.UniformThetaApproximationGenerator
import ru.ispras.modis.tm.matrix.AttributedPhi
import ru.ispras.modis.tm.stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 06.05.14
 * Time: 15:54
 */
class FixedPhiBuilder(alphabet: Alphabet,
                      documents: Array[Document],
                      numberOfIterations: Int,
                      private val phi: Map[AttributeType, AttributedPhi],
                      attributeWeight: Map[AttributeType, Float] = Map[AttributeType, Float]())
    extends AbstractPLSABuilder(phi.head._2.numberOfRows, alphabet, documents, attributeWeight, false) {

    override protected def buildBricks(modelParameters: ModelParameters): Map[AttributeType, AbstractPLSABrick] = {
        modelParameters.numberOfWords.map { case (attribute, numberOfWords) =>
            (attribute, new NonRobustPhiFixedBrick(attribute, modelParameters, attributeWeight.getOrElse(attribute, 1f)))
        }
    }

    setInitialApproximationGenerator(new UniformThetaApproximationGenerator(phi))

    setStoppingCriteria(new MaxNumberOfIterationStoppingCriteria(numberOfIterations))
}
