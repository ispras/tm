package ru.ispras.modis.tm.plsa

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.objenesis.strategy.StdInstantiatorStrategy

/**
 * Created by valerij on 7/1/14.
 */
object TrainedModelSerializer {
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
