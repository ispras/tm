package ru.ispras.modis.tm.utils

import java.io.{FileInputStream, FileOutputStream}
import java.util.zip.{GZIPOutputStream, GZIPInputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.objenesis.strategy.StdInstantiatorStrategy

import scala.reflect.ClassTag

/**
 * Created by valerij on 06.12.14
 */
trait GenericSerializer {

    protected val kryo : Kryo

    protected def genericLoad[T : ClassTag](path: String) : T = {
        val input = getInput(path)
        val smth = genericLoad(input)
        input.close()
        smth
    }

    private def getInput(path: String) = {
        val fileInput = new FileInputStream(path)
        if (path.endsWith(".gz"))
            new Input(new GZIPInputStream(fileInput))
        else
            new Input(fileInput)
    }

    protected def genericLoad[T : ClassTag](input : Input) : T = {
        kryo.readObject(input, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
    }

    protected def genericSave[T](smth : T, path: String) : Unit = {
        val output = getOutput(path)
        kryo.writeObject(output, smth)
        output.close()
    }

    private def getOutput(path: String) = {
        if (path.endsWith(".gz"))
            new Output(new GZIPOutputStream(new FileOutputStream(path)))
        else
            new Output(new FileOutputStream(path))
    }
}
