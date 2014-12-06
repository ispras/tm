package ru.ispras.modis.tm.utils

import ru.ispras.modis.tm.attribute.{AttributeType, DefaultAttributeType}

/**
 * Created by valerij on 06.12.14.
 */
trait DefaultChecker {
    protected def checkDefault[K](map : Map[AttributeType, K]) {
        require(map.size == 1, "do not use this method with more than one attribute")
        require(map.contains(DefaultAttributeType), "does not contain default attribute type")
    }
}
