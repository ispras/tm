package ru.ispras.modis.plsa

import ru.ispras.modis.matrix.{Theta, AttributedPhi}
import ru.ispras.modis.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:51
 */
/**
 * store distribution of words by topic and documents by topics
 * @param phi distribution of words by topic for every attribute
 * @param theta distribution of document by topic
 */
class TrainedModel(val phi: Map[AttributeType, AttributedPhi], val theta: Theta)
