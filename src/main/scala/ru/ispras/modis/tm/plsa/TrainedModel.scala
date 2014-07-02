package ru.ispras.modis.tm.plsa

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.objenesis.strategy.StdInstantiatorStrategy
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
 * @param theta distribution of document by topic
 */
class TrainedModel(val phi: Map[AttributeType, AttributedPhi], val theta: Theta) {
    def this() = this(Map[AttributeType, AttributedPhi](), null)

    def getPhi = {
        require(phi.size == 1, "there is more than one attribute, use .phi(attribute)")
        require(phi.contains(DefaultAttributeType), "there is no default attribute")

        phi(DefaultAttributeType)
    }
}

