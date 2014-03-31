package plsa

import matrix.{Theta, AttributedPhi}
import attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.03.14
 * Time: 16:51
 */
class TrainedModel(val phi: Map[AttributeType, AttributedPhi], val theta: Theta)
