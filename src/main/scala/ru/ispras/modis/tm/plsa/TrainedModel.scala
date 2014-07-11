package ru.ispras.modis.tm.plsa

import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:51
 */
/**
 * store distribution of words by topic and documents by topics
 * @param phi distribution of words by topic for every attribute
 * @param perplexity perplexity value obtained on the training of this model
 * @param theta distribution of document by topic
 */
class TrainedModel(val phi: Map[AttributeType, AttributedPhi], val theta: Theta, perplexity: Double) {

    def getPhi = {
        require(phi.size == 1, "there is more than one attribute, use .phi(attribute)")
        require(phi.contains(DefaultAttributeType), "there is no default attribute")

        phi(DefaultAttributeType)
    }

    def getPhi(attribute: AttributeType) = {
        require(phi.contains(attribute), "there is no attribute " + attribute)
        phi(attribute)
    }
}

