package plsa

import documents.Document

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 18:09
 */
abstract class TopicModel {
    def train(documents: Seq[Document]): TrainedModel
}
