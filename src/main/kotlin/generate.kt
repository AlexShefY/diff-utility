import kotlin.math.max
import kotlin.math.min
const val size: Int = 5000 // константа, от которой зависит размер тестов
/*
 * Содержимое данного фалйа предназначено для рандомной генерации тестов
 */

/*
 * Функция, добавляющая в текст рандомные строки
 */
fun randomAdd(text2 : MutableList<String>, text_ : MutableList<String>) : MutableList<String> {
    var cntAdd = (0..size).random()
    while(cntAdd > 0){
        var idx1 = (0..text2.size).random()
        var idx2 = (text_.indices).random()
        text2.add(idx1, text_[idx2])
        cntAdd--
    }
    return text2
}
/*
 * Функция, удаляющая из текста рандомные строки
 */
fun randomRemove(text : MutableList<String>) : MutableList<String>{
    var cntDel = min(max(0, text.size - 8000), size) / 2
    cntDel = (0..cntDel).random()
    while (cntDel > 0) {
        text.removeAt((text.indices).random())
        cntDel--
    }
    return text
}
/*
 * Функция,рандомно меняющая соседние строки местами
 */
fun randomSwap(text : MutableList<String>) : MutableList<String>{
    var cntSwap = (0..size).random()
    while(cntSwap > 0 && text.size > 0){
        var idx1 = (text.indices).random()
        var idx2 = (text.indices).random()
        var c = text[idx2]
        text[idx2] = text[idx1]
        text[idx1] = c
        cntSwap--
    }
    return text
}


/*
 * Функция, генерирующая рандомные тестовые тексты
 */
fun generate() : Pair<MutableList<String>, MutableList<String>>{
    var text = inputOneFile("file7.txt") // из этого файла берем набор строк, из которых будут сгенерированы тексты
    var text1 = mutableListOf<String>()
    var n : Int = size
    var n1 : Int = ((n - 100)..n).random()
    var n2 : Int = (0..((n - n1))).random()
    for (j in 0 until n1) {
        var idx: Int = (1..text.size).random() - 1
        text1.add(text[idx])
    }
    var text2 = text1
    text2 = randomAdd(text2, text)
    text2 = randomRemove(text2)
    text2 = randomSwap(text2)
    return Pair(text1, text2)
}