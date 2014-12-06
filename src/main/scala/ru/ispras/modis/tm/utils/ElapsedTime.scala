package ru.ispras.modis.tm.utils

import grizzled.slf4j.Logging

/**
 * Created by valerij on 06.12.14.
 */
trait ElapsedTime extends Logging {
    def time[T](message : String)(op :  => T) = {
        val start = System.currentTimeMillis()
        val result = op
        val seconds: Double = System.currentTimeMillis() * 0.001 - start / 1000
        info(message + " took " + ("%1.2f" format seconds) + " seconds")
        result
    }
}
