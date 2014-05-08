package plsa

import matrix.{Theta, AttributedPhi}
import main.scala.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:51
 */
/**
 * store distribution of words by topic and main.scala.documents by topics
 * @param phi distribution of words by topic for every main.scala.attribute
 * @param theta distribution of document by topic
 */
class TrainedModel(val phi: Map[AttributeType, AttributedPhi], val theta: Theta)
