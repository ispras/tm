package ru.ispras.modis.tm.initialapproximationgenerator

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.Ogre._
import ru.ispras.modis.tm.matrix.{AttributedPhi, Ogre, Theta}
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 02.04.14
 * Time: 17:03
 */
class GibbsInitialApproximationGenerator(private val random: Random) extends PhiThetaApproximationGenerator {
    protected def fillMatrix(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: Map[AttributeType, AttributedPhi]) {
        phi.values.foreach(matrix => processAttribute(parameters, documents, theta, matrix))
        smooth(theta)
        phi.foreach { case (attribute, matrix) => smooth(matrix)}
    }

    private def processAttribute(parameters: ModelParameters, documents: Seq[Document], theta: Theta, phi: AttributedPhi) {
        for (document <- documents if document.contains(phi.attribute)) {
            processSingleDocument(parameters, document, theta, phi)
        }
    }

    private def processSingleDocument(parameters: ModelParameters, document: Document, theta: Theta, phi: AttributedPhi) {
        var word = 0
        for ((wordIndex, wordNum) <- document.getAttributes(phi.attribute)) {
            while (word < wordNum) {
                val topic = random.nextInt(parameters.numberOfTopics)
                phi.addToExpectation(topic, wordIndex, 1)
                theta.addToExpectation(document.serialNumber, topic, 1)
                word += 1
            }
            word = 0
        }
    }

    private def smooth(matrix: Ogre) {
        matrix.addToExpectation(random.nextFloat())
    }
}
