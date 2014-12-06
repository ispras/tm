package ru.ispras.modis.tm.documents

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import gnu.trove.map.hash.{TIntObjectHashMap, TObjectIntHashMap}
import org.objenesis.strategy.StdInstantiatorStrategy
import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.utils.GenericSerializer

/**
 * Created by valerij on 06.12.14.
 */
object AlphabetSerializer extends GenericSerializer {
    protected val kryo = new Kryo
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy)

    def save(alphabet : Alphabet, path: String) = {
        val output = new Output(new FileOutputStream(path))

        kryo.writeObject(output, alphabet.getAttributes().size)

        for (attribute <- alphabet.getAttributes()) {
            val words = getWords(alphabet, attribute)
            kryo.writeClassAndObject(output, attribute)
            kryo.writeObject(output, words)
        }

        output.close()
    }

    private def getWords(alphabet : Alphabet, attribute : AttributeType) : Array[String] = (0 until alphabet.numberOfWords(attribute)).map(alphabet(attribute, _)).toArray

    def load(path: String) : Alphabet =  {
        val input = new Input(new FileInputStream(path))
        val numberOfAttributes = genericLoad[Int](input)

        val attr2Maps = (0 until numberOfAttributes).map(i => {
            val attr = kryo.readClassAndObject(input)
            val words = genericLoad[Array[String]](input)

            (attr.asInstanceOf[AttributeType], wordsToMaps(words))
        })

        new Alphabet(attr2Maps.map{case(attr, (w2i, i2w)) => (attr, i2w) }.toMap, attr2Maps.map{case(attr, (w2i, i2w)) => (attr, w2i) }.toMap )
    }

    def wordsToMaps(words : Array[String]) = {
        val word2Index = new TObjectIntHashMap[String]()
        val index2word = new TIntObjectHashMap[String]()

        for ((word, index) <- words.zipWithIndex) {
            word2Index.put(word, index)
            index2word.put(index, word)
        }

        (word2Index, index2word)
    }

}
