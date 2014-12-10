package ru.ispras.modis.tm.utils

import grizzled.slf4j.Logging
import org.joda.time.Duration
import org.joda.time.format.PeriodFormatterBuilder

/**
 * Created by valerij on 06.12.14.
 */
trait ElapsedTime extends Logging {
    private val formatter = new PeriodFormatterBuilder()
      .appendHours()
      .appendSuffix("h")
      .appendMinutes()
      .appendSuffix("m")
      .appendSeconds()
      .appendSuffix("s")
      .toFormatter()


    def time[T](message : String)(op :  => T) = {
        val start = System.currentTimeMillis()
        val result = op
        val elapsedMilliseconds = System.currentTimeMillis() - start

        val duration = new Duration(elapsedMilliseconds)

        info(message + " took " + formatter.print(duration.toPeriod))
        result
    }
}
