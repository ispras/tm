package ru.ispras.modis.tm.plsa

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.objenesis.strategy.StdInstantiatorStrategy
import ru.ispras.modis.tm.matrix.{Theta, AttributedPhi}
import ru.ispras.modis.tm.attribute.{DefaultAttributeType, Category, AttributeType}

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

    def getPhi = phi(DefaultAttributeType)
}

object TrainedModel {
    def save(model: TrainedModel, path: String) {
        val kryo = new Kryo
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy)
        val output = new Output(new FileOutputStream(path))
        kryo.writeObject(output, model)
        output.close()
    }

    def load(path: String) = {
        val kryo = new Kryo
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy)
        val input = new Input(new FileInputStream(path))
        val trainedModel = kryo.readObject(input, classOf[TrainedModel])
        input.close()
        trainedModel
    }
}
