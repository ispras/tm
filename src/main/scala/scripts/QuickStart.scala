package scripts

import documents.{Numerator, TextualDocument}
import scala.io.Source
import java.io.File
import builder.PLSABuilder
import java.util.Random
import utils.TopicProcessing
import attribute.Category

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 30.04.14
 * Time: 16:11
 */

object QuickStart extends App{

    /**
     * first of all we have to read textual main.scala.documents.
     * Textual document is a sequence of words(String)
     * We read textual main.scala.documents from a textual file, one document per line. Words are separated by space.
     * Documents should be preprocessed.
     */
    def getTextualDocuments(): Iterator[TextualDocument] = {
        /**
         * read lines from textual file with a 1000 scientific articles
         */
        val lines = Source.fromFile(new File("examples/arxiv.part")).getLines()

        /**
         * split each line by space
         */
        val wordsSequence = lines.map(line => line.split(" "))

        /**
         * now we obtain a sequence of sequence of words and should to construct textual main.scala.documents.
         * Textual document may contain a few texts, corresponding to different  attributes,
         * for example text in english, translation of this text to russian etc. If you document contain only one text
         * you may use main.scala.attribute Category
         */
        val textualDocuments = wordsSequence.map(words => new TextualDocument(Map(Category -> words)))

        /**
         * and now we return sequence of textual main.scala.documents
         */
        textualDocuments
    }



    /**
     * read textual main.scala.documents from file (see functions getTextualDocuments for details)
     */
    val textualDocuments = getTextualDocuments()

    /**
     * now we have to replace words by it serial number. For this purposes we would use object Numerator
     * Numerator take into input iterator of TextualDocuments, replace words by it serial numbers and return
     * sequence of main.scala.documents and instance of class alphabet (alphabet hold map from wordsNumber to word and vice versa)
     */
    val (documents, alphabet) = Numerator(textualDocuments)


    /**
     * now we have to build model. In this example we would use a main.scala.plsa
     * we use main.scala.builder to build instance of class PLSA
     * it require define number of topics, number of iterations, alphabet, sequence of main.scala.documents and random number generator
     * to generate initial approximation
     */
    val numberOfTopics = 25
    val numberOfIteration = 100 // number of iteration in EM algorithm
    val random = new Random() // java.util.Random
    val builder = new PLSABuilder(numberOfTopics, alphabet, documents, random,  numberOfIteration)

    /**
     * and now we build main.scala.plsa
     */
    val plsa  = builder.build()

    /**
     * now we have main.scala.documents and model and we may train model
     */
    val trainedModel = plsa.train(documents)

    /**
     * now we obtain main.scala.matrix of distribution of words by topics and we may see most popular words from each topic
     */
    val n = 10 // number of top words to see
    TopicProcessing.printAllTopics(n, trainedModel.phi(Category), alphabet)

    /**
     * now we save main.scala.matrix Phi (words by topic ) into file examples/Phi
     * and main.scala.matrix Theta (topic by document) in file examples/Theta
     */
    TopicProcessing.saveMatrix("examples/Phi", trainedModel.phi(Category))
    TopicProcessing.saveMatrix("examples/Theta", trainedModel.theta)
}
