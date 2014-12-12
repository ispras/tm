package ru.ispras.modis.tm.initialapproximationgenerator

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Ogre, Theta}
import ru.ispras.modis.tm.utils.ModelParameters
import ru.ispras.modis.tm.matrix.Ogre._

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.03.14
 * Time: 15:27
 */
/**
 * generate random initial approximation. Full Phi and Theta by uniform distributed random value
 * @param random random number generator
 */
class RandomInitialApproximationGenerator(private val random: Random) extends PhiThetaApproximationGenerator {

    /**
     * full expectation matrix by the random numbers from uniform distribution
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     * @param phi matrix with zero values in expectation and stochastic matrix
     */
    protected def fillMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]) {
        fillSingleMatrix(theta)
        phi.foreach { case (attribute, matrix) => fillSingleMatrix(matrix)}
    }

    private def fillSingleMatrix(matrix: Ogre) {
        matrix.addToExpectation(random.nextFloat() + 2 * math.sqrt(Float.MinPositiveValue).toFloat)
    }
}
