package plsa

import stoppingcriteria.StoppingCriteria
import attribute.AttributeType
import brick.AbstractPLSABrick
import sparsifier.Sparsifier
import regularizer.Regularizer
import matrix.{Theta, AttributedPhi}

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 01.04.14
 * Time: 18:01
 */
class PLSABuilder(private val bricks: Map[AttributeType, AbstractPLSABrick],
                  private val stoppingCriteria: StoppingCriteria,
                  private val thetaSparsifier: Sparsifier,
                  private val regularizer: Regularizer,
                  private val phi: Map[AttributeType, AttributedPhi],
                  private val theta: Theta)
