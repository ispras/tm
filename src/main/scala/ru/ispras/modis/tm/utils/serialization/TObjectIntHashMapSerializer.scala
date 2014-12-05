package ru.ispras.modis.tm.utils.serialization

import com.esotericsoftware.kryo.{Kryo, Serializer}
import gnu.trove.map.hash.{TIntObjectHashMap, TObjectIntHashMap, TIntIntHashMap}
import com.esotericsoftware.kryo.io.{Input, Output}

/**
 * Created with IntelliJ IDEA.
 * User: valerij
 * Date: 6/20/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */

class TObjectIntHashMapSerializer extends Serializer[TObjectIntHashMap[Object]] {
    def write(kryo: Kryo, out: Output, map: TObjectIntHashMap[Object]) {
        val values = map.values()
        val keys = map.keys()
        kryo.writeObject(out, values)
        kryo.writeObject(out, keys)
    }

    def read(kryo: Kryo, in: Input, clazz: Class[TObjectIntHashMap[Object]]): TObjectIntHashMap[Object] = {
        val values = kryo.readObject(in, classOf[Array[Int]])
        val keys = kryo.readObject(in, classOf[Array[Object]])

        val map = new TObjectIntHashMap[Object]()

        keys.zip(values).map {
            case (key, value) => map.put(key, value)
        }
        map
    }
}
