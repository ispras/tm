package qualitimeasurment

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 08.04.14
 * Time: 15:15
 */

class Bigram(val word: Int, val otherWord: Int)  {
    override def equals(obj: Any): Boolean = obj match {
        case obj: Bigram => (word == obj.word && otherWord == obj.otherWord)  ||  (word == obj.otherWord && otherWord == obj.word)
        case _ => false
    }

    override def hashCode(): Int = super.hashCode() + word.hashCode() * 17 + otherWord.hashCode() * 17
}

