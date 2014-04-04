package scripts

import documents.{TextualDocument, Numerator}
import java.util.Random
import scala.io.Source
import java.io.File
import attribute.Category
import builder.{PLSABuilder, RobustPLSABuilder}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 28.03.14
 * Time: 13:49
 */
object PigData extends App {
    def getDocs(path: String) = {

        val lines = Source.fromFile(new File(path)).getLines().take(100000).map(line => new TextualDocument(Map(Category -> line.split(" "))))
        val random = new Random
        random.setSeed(13)
        val (docs, alphabet) = Numerator(lines.toSeq)

        val plsa = new RobustPLSABuilder(10, alphabet, docs, random, 33, 0f, 0f).build()
        (plsa, docs)
    }


    val readDataTime = System.currentTimeMillis()
    val (plsa, docs) = getDocs("/media/3d6a5a46-cbd3-49cd-abd4-2907eed0831a/home/padre/data/PigData/dde1000")
    println(" readDataTime " + (System.currentTimeMillis() * 0.001 - readDataTime / 1000))
    val start = System.currentTimeMillis()
    val trainedModel = plsa.train(docs)
    println(trainedModel)
    println(" time " + (System.currentTimeMillis() * 0.001 - start / 1000))
}
