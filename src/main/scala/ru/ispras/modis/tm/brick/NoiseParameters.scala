package ru.ispras.modis.tm.brick

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 21.03.14
 * Time: 19:45
 */
/**
 * store parameters for robust plsa. Background is a common words which can be explain by any topic
 * noise is a very specific words, which can not be explain by any topic
 * @param gamma noise weight.
 * @param eps background weight
 */
class NoiseParameters(private val gamma: Float, private val eps: Float) {
    require(gamma >= 0 && eps >= 0, "noise and background weights must be non negative")

    /**
     *
     * @return parameter gamma
     */
    def noiseWeight = gamma

    /**
     *
     * @return parameter epsilon
     */
    def backgroundWeight = eps
}
