package ru.ispras.modis.tm.utils

import java.io.{FileInputStream, FileOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.objenesis.strategy.StdInstantiatorStrategy

import scala.reflect.ClassTag

/**
 * Created by valerij on 06.12.14.
 */
trait GenericSerializer {
    protected val kryo : Kryo

    protected def genericLoad[T : ClassTag](path: String) : T = {
        val input = new Input(new FileInputStream(path))
        val smth = genericLoad(input)
        input.close()
        smth
    }

    protected def genericLoad[T : ClassTag](input : Input) : T = {
        kryo.readObject(input, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
    }

    protected def genericSave[T](smth : T, path: String) : Unit = {
        val output = new Output(new FileOutputStream(path))
        kryo.writeObject(output, smth)
        output.close()
    }
}
