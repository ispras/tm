package ru.ispras.modis.tm.scripts

import java.io.File
import java.util.Random

import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.builder.PLSABuilder
import ru.ispras.modis.tm.documents.{Numerator, SingleAttributeNumerator, TextualDocument}
import ru.ispras.modis.tm.regularizer.TopicEliminatingRegularizer
import ru.ispras.modis.tm.sparsifier.CarefulSparcifier
import ru.ispras.modis.tm.utils.TopicHelper

import scala.io.Source

/**
 * Created by valerij on 7/9/14.
 */
object TopicNumberSelection extends App {

    /**
     * first of all we have to read textual documents.
     * Textual document is a sequence of words(String)
     * We read textual documents from a textual file, one document per line. Words are separated by space.
     * Documents should be preprocessed.
     */
    def getTextualDocuments(): Iterator[TextualDocument] = {
        val lines = Source.fromFile(new File("examples/arxiv.part")).getLines().take(3000)

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
        val textualDocuments = wordsSequence.map(words => new TextualDocument(Map(DefaultAttributeType -> words)))

        /**
         * and now we return sequence of textual documents
         */
        textualDocuments
    }


    /**
     * read textual documents from file (see functions getTextualDocuments for details)
     */
    val textualDocuments = getTextualDocuments()
//        Seq("a a b b c c d d d ".split(' ').toSeq, "z z z x x x y y y ".split(' ').toSeq, "z z x x a a b b".split(' ').toSeq)

    val (documents, alphabet) = Numerator(textualDocuments)

    val numberOfTopics = 60
    val numberOfIteration = 1000
    // number of iteration in EM algorithm
    val random = new Random(13)
    // java.util.Random
    val builder = new PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration)
        .addRegularizer(new TopicEliminatingRegularizer(documents, 500))
        .setThetaSparsifier(new CarefulSparcifier(0.1f, 15, 2))
    //        .addRegularizer(new DecorrelatingRegularizer(10))
    //        .addRegularizer(new SymmetricDirichlet(-0.5f, -0.1f))


    val plsa = builder.build()

    val trainedModel = plsa.train

    private val significant = TopicHelper.getSignificantTopics(trainedModel.theta)
    println(significant.size)
    println(significant)

    val n = 10 // number of top words to see
    TopicHelper.printAllTopics(n, trainedModel.phi(DefaultAttributeType), alphabet)

}
