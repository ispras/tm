package ru.ispras.modis.tm.utils.serialization

import com.esotericsoftware.kryo.{Kryo, Serializer}
import gnu.trove.map.hash.TIntObjectHashMap
import com.esotericsoftware.kryo.io.{Input, Output}

/**
 * Created with IntelliJ IDEA.
 * User: valerij
 * Date: 6/20/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
class TIntObjectHashMapSerializer extends Serializer[TIntObjectHashMap[Object]] {
    def write(kryo: Kryo, out: Output, map: TIntObjectHashMap[Object]) {
        val values = map.values
        val keys = map.keys()
        kryo.writeObject(out, values)
        kryo.writeObject(out, keys)
    }

    def read(kryo: Kryo, in: Input, clazz: Class[TIntObjectHashMap[Object]]): TIntObjectHashMap[Object] = {
        val values = kryo.readObject(in, classOf[Array[Object]])
        val keys = kryo.readObject(in, classOf[Array[Int]])

        val map = new TIntObjectHashMap[Object]()

        keys.zip(values).map {
            case (key, value) => map.put(key, value)
        }
        map
    }
}
