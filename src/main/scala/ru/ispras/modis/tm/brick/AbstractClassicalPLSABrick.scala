package ru.ispras.modis.tm.brick

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.matrix.{AttributedPhi, Theta}
import ru.ispras.modis.tm.regularizer.Regularizer
import ru.ispras.modis.tm.sparsifier.Sparsifier
import ru.ispras.modis.tm.utils.ModelParameters
import scala.collection.par._
import scala.collection.par.Scheduler.Implicits.global
import scala.collection.optimizer._

/**
 * Created by valerij on 12/4/14.
 */
abstract class AbstractClassicalPLSABrick(regularizer: Regularizer,
                                phiSparsifier: Sparsifier,
                                attribute: AttributeType,
                                modelParameters: ModelParameters,
                                attributeWeight: Float,
                                parallel : Boolean = false)
    extends AbstractPLSABrick(regularizer, phiSparsifier, attribute, modelParameters, attributeWeight) {

    protected def processCollection(theta: Theta, phi: AttributedPhi, documents: Array[Document]) : Double = optimize {
        var logLikelihood = 0d

        if (parallel) {
            documents.toPar.aggregate(0d)( _ + _)((sum, doc) => sum + (if (doc.contains(attribute)) processSingleDocument(doc, theta, phi) else 0d) )
        } else {
            for (doc <- documents if doc.contains(attribute)) {
                logLikelihood += processSingleDocument(doc, theta, phi)
            }
        }
        logLikelihood
    }

    protected def processSingleDocument(document: Document, theta: Theta, phi: AttributedPhi) : Double


}
