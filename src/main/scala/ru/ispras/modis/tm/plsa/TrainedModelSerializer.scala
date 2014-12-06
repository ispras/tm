package ru.ispras.modis.tm.plsa

import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy
import ru.ispras.modis.tm.utils.GenericSerializer

/**
 * Created by valerij on 7/1/14.
 */
object TrainedModelSerializer extends GenericSerializer {
    protected val kryo = new Kryo
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy)

    def save(model: TrainedModel, path: String) = genericSave(model, path)

    def load(path: String) : TrainedModel = genericLoad[TrainedModel](path)
}



