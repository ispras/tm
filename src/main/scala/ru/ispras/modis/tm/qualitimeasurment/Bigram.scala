package ru.ispras.modis.tm.qualitimeasurment

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 08.04.14
 * Time: 15:15
 */

/**
 * hold pair of words. Order is not important
 * @param word serial number of the first word
 * @param otherWord serial number of the second word
 */
class Bigram(val word: Int, val otherWord: Int) {
    override def equals(obj: Any): Boolean = obj match {
        case obj: Bigram => (word == obj.word && otherWord == obj.otherWord) || (word == obj.otherWord && otherWord == obj.word)
        case _ => false
    }

    override def hashCode(): Int = word.hashCode() * 17 + otherWord.hashCode() * 17 + math.abs(word - otherWord)
}

