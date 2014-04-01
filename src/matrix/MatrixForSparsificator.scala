package matrix

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.03.14
 * Time: 20:58
 */
class MatrixForSparsifier(private val data: Array[Array[Float]]) {
    private var unchanged = true

    def isNormalised = unchanged

    def apply(rowIndex: Int, columnIndex: Int) = data(rowIndex)(columnIndex)

    def update(rowIndex: Int, columnIndex: Int, value: Float) {
        //TODO SET_ZERO!!!
        unchanged = false
        data(rowIndex)(columnIndex) = value
    }
}
