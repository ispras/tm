package ru.ispras.modis.scripts

import ru.ispras.modis.documents.{Numerator, TextualDocument}
import ru.ispras.modis.plsa.TrainedModel
import scala.io.Source
import java.io.File
import ru.ispras.modis.builder.PLSABuilder
import java.util.Random
import ru.ispras.modis.utils.TopicHelper
import ru.ispras.modis.attribute.Category

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 30.04.14
 * Time: 16:11
 */

object QuickStart extends App{

    /**
     * first of all we have to read textual documents.
     * Textual document is a sequence of words(String)
     * We read textual documents from a textual file, one document per line. Words are separated by space.
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
         * now we obtain a sequences of sequence of words and should to construct textual documents.
         * Textual document may contain a few texts, corresponding to different  attributes,
         * for example text in english, translation of this text to russian etc. If you document contain only one text
         * you may use attribute Category
         */
        val textualDocuments = wordsSequence.map(words => new TextualDocument(Map(Category -> words)))

        /**
         * and now we return sequence of textual documents
         */
        textualDocuments
    }



    /**
     * read textual documents from file (see functions getTextualDocuments for details)
     */
    val textualDocuments = getTextualDocuments()
    /**
     * now we have to replace words by it serial number. For this purposes we would use object Numerator
     * Numerator take into input iterator of TextualDocuments, replace words by it serial numbers and return
     * sequence of documents and instance of class alphabet (alphabet hold map from wordsNumber to word and vice versa)
     */
    val (documents, alphabet) = Numerator(textualDocuments)

    /**
     * val splitLines = Source.fromFile(new File("examples/arxiv.part")).getLines().map(_.split(" "))
     * val (documents, alphabet) = Numerator(splitLines)
     */




    /**
     * now we have to build model. In this example we would use a plsa
     * we use builder to build instance of class PLSA
     * it require define number of topics, number of iterations, alphabet, sequence of documents and random number generator
     * to generate initial approximation
     */
    val numberOfTopics = 25
    val numberOfIteration = 100 // number of iteration in EM algorithm
    val random = new Random() // java.util.Random
    val builder = new PLSABuilder(numberOfTopics, alphabet, documents, random,  numberOfIteration)

    /**
     * and now we build plsa
     */
    val plsa  = builder.build()

    /**
     * now we have documents and model and we may train model. Our model take into input sequence of documents and
     * perform stochastic matrix decomposition F ~ Phi * Theta where Phi is distribution of words by topics, thus
     * the number in the intersects of i-th row and j-th column show the probability to generate word j from topic i.
     * Theta is distribution of document by topic thus the number in the intersects of i-th row and j-th column show
     * the weight of topic j in document i.
     * The result would be saved in TrainedModel
     */
    val trainedModel = plsa.train(documents)

    /**
     * now we obtain matrix of distribution of words by topics and we may see most popular words from each topic
     * For this purpose we use util printAllTopics. It print n words with the highest probability from every topic.
     */
    val n = 10 // number of top words to see
    TopicHelper.printAllTopics(n, trainedModel.phi(Category), alphabet)

    /**
     * now we save matrix Phi (words by topic ) into file examples/Phi
     * and matrix Theta (topic by document) in file examples/Theta
     */
    TopicHelper.saveMatrix("examples/Phi", trainedModel.phi(Category))
    TopicHelper.saveMatrix("examples/Theta", trainedModel.theta)

    /**
     * and now we can serialize trainedModel using kryo. Kryo saves objects in binary format, so do not try to open
     * model by textual redactor.
     */
    TrainedModel.save(trainedModel, "examples/model")

    /**
     * and now we may load model
     */
    TrainedModel.load("examples/model").getPhi.probability(1, 1)

}
