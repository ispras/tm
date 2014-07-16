package ru.ispras.modis.tm.scripts

import java.util.Random

import ru.ispras.modis.tm.attribute.DefaultAttributeType
import ru.ispras.modis.tm.builder.PLSABuilder
import ru.ispras.modis.tm.documents.SingleAttributeNumerator
import ru.ispras.modis.tm.regularizer.TopicEliminatingRegularizer
import ru.ispras.modis.tm.sparsifier.CarefulSparcifier
import ru.ispras.modis.tm.utils.TopicHelper

/**
 * Created by valerij on 7/9/14.
 */
object TopicNumberSelection extends App {

    /**
     * these methods define a simple way to generate a a collection via three topics.
     * topics with tokens ("a", "b", "c", "d"), ("z", "x", "y", "q") and  ("a", "b", "z", "x").
     *
     * We assume  that probabilities p(w|t) are equal for every word given topic
     */
    def generateDoc(letters: Vector[String], random: Random) = (0 until 100).map(i => letters(random.nextInt(letters.size)))

    def generateCollection(random: Random) = (0 until 1000).map(i => generateDoc(Vector("a", "b", "c", "d"), random)) ++
        (0 until 1000).map(i => generateDoc(Vector("z", "x", "y", "q"), random)) ++
        (0 until 1000).map(i => generateDoc(Vector("a", "b", "z", "x"), random))

    /**
     * numeration is performed as usual (see QuickStart)
     */
    val random = new Random(13)
    val textualDocuments = generateCollection(random).iterator

    val (documents, alphabet) = SingleAttributeNumerator(textualDocuments)

    /**
     * Note. We start from 60 topics -- that's excessive number of topics for our collection
     */
    val numberOfTopics = 60
    val numberOfIteration = 100

    /**
     * Now the fun begins
     *
     * We are to add TopicEliminatingRegularizer in order to reduce the significance of not-so-good topics
     * (topics that don't explain collection well) in matricies theta.
     * It's wise to use theta sparicifier here, because in order to eliminate topic, we should set  theta_{dt} forall d equal zero ,
     * but TopicEliminatingRegularizer usually reduces the value theta_{dt} but does not set it to zero
     *
     */
    val builder = new PLSABuilder(numberOfTopics, alphabet, documents, random, numberOfIteration)
        .addRegularizer(new TopicEliminatingRegularizer(documents, 2000))
        .setThetaSparsifier(new CarefulSparcifier(0.1f, 15, 2))

    val plsa = builder.build()

    /**
     * train the model. Note, you'll get warnings "sum should be > 0" -- that means that useless topics were found.
     *
     * method densifyModel gets rid of useless topics, so number of topics in trainedModel should be much less that 60
     */
    val trainedModel = TopicHelper.densifyModel(plsa.train)

    TopicHelper.printAllTopics(4, trainedModel.phi(DefaultAttributeType), alphabet)

}
