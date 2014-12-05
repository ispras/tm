package ru.ispras.modis.tm.plsa

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import gnu.trove.map.hash.{TIntObjectHashMap, TObjectIntHashMap}
import org.objenesis.strategy.StdInstantiatorStrategy
import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.documents.Alphabet
import ru.ispras.modis.tm.utils.serialization.{TIntObjectHashMapSerializer, TObjectIntHashMapSerializer}

import scala.reflect.ClassTag

/**
 * Created by valerij on 7/1/14.
 */
object TrainedModelSerializer {
    private val kryo = new Kryo

    kryo.register(classOf[TObjectIntHashMap[String]], new TObjectIntHashMapSerializer, 13)
    kryo.register(classOf[TIntObjectHashMap[String]], new TIntObjectHashMapSerializer, 14)
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy)

    def save(model: TrainedModel, path: String) = genericSave(model, path)

    def save(alphabet : Alphabet, path: String) = {
        val words = (0 until alphabet.numberOfWords(DefaultAttributeType)).map(alphabet(_)).toArray
        genericSave(words, path)
    }

    private def genericSave[T](smth : T, path: String) : Unit = {
        val output = new Output(new FileOutputStream(path))
        kryo.writeObject(output, smth)
        output.close()
    }

    def load(path: String) : TrainedModel = genericLoad[TrainedModel](path)

    def loadAlphabet(path: String) : Alphabet =  {
        val words = genericLoad[Array[String]](path)

        val word2Index = new TObjectIntHashMap[String]()
        val index2word = new TIntObjectHashMap[String]()

        for ((word, index) <- words.zipWithIndex) {
            word2Index.put(word, index)
            index2word.put(index, word)
        }

        new Alphabet(Map(DefaultAttributeType -> index2word) , Map(DefaultAttributeType -> word2Index))
    }

    def genericLoad[T : ClassTag](path: String) : T = {
        val input = new Input(new FileInputStream(path))
        val smth = kryo.readObject(input, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
        input.close()
        smth
    }

}



