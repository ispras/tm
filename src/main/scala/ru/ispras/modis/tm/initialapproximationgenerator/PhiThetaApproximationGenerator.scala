package ru.ispras.modis.tm.initialapproximationgenerator

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 24.03.14
 * Time: 18:39
 */
/**
 * Generate initial approximation for distribution of words by topic (main.scala.matrix phi) and distribution of
 * document by topic (main.scala.matrix theta)
 */
trait PhiThetaApproximationGenerator extends InitialApproximationGenerator {
    /**
     * generate matrices phi and theta, full expectation with function fullMatrix and dump.
     * @param parameters parameter of models contain size of matrices
     * @param documents sequence of inputdocuments
     * @return initializedmatrix theta and phi
     */
    def apply(parameters: ModelParameters, documents: Array[Document]): (Theta, Map[AttributeType, AttributedPhi]) = {
        val theta = Theta(createMatrix(documents.length, parameters.numberOfTopics))
        val phi = parameters.numberOfWords.map {
            case (attribute, numberOfWords) => (attribute, AttributedPhi(createMatrix(parameters.numberOfTopics, numberOfWords), attribute))
        }

        fillMatrix(parameters, documents, theta, phi)
        theta.dump()
        phi.foreach { case (attribute, matrix) => matrix.dump()}
        (theta, phi)
    }

    /**
     * this method full matrices phi and theta by some initial values.
     * !WARNING! do NOT do dump before return matrices
     * @param parameters model parameters
     * @param documents sequence ofdocuments
     * @param theta matrix with zero values in expectation and stochastic matrix
     * @param phi matrix with zero values in expectation and stochastic matrix
     */
    protected def fillMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]): Unit

}
