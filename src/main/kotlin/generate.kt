import kotlin.math.max
import kotlin.math.min
val m : Int = 5000
/*
 * Содержимое данного фалйа предназначено для рандомной генерации тестов
 */

/*
 * Функция, добавляющая в текст рандомные строки
 */
fun random_add(text2 : MutableList<String>, text_ : MutableList<String>) : MutableList<String> {
    var m_add = (0..m).random()
    while(m_add > 0){
        var idx1 = (0..text2.size).random()
        var idx2 = (text_.indices).random()
        text2.add(idx1, text_[idx2])
        m_add--
    }
    return text2
}
/*
 * Функция, удаляющая из текста рандомные строки
 */
fun random_remove(text : MutableList<String>) : MutableList<String>{
    var m_del = min(max(0, text.size - 8000), m) / 2
    m_del = (0..m_del).random()
    while (m_del > 0) {
        text.removeAt((text.indices).random())
        m_del--
    }
    return text
}
/*
 * Функция,рандомно меняющая соседние строки местами
 */
fun random_swap(text : MutableList<String>) : MutableList<String>{
    var m_swap = (0..m).random()
    while(m_swap > 0 && text.size > 0){
        var idx1 = (text.indices).random()
        var idx2 = (text.indices).random()
        var c = text[idx2]
        text[idx2] = text[idx1]
        text[idx1] = c
        m_swap--
    }
    return text
}


/*
 * Функция, генерирующая рандомные тестовые тексты
 */
fun generate() : Pair<MutableList<String>, MutableList<String>>{
    var text_ = inputOneFile("file7.txt") // из этого файла берем набор строк, из которых будут сгенерированы тексты
    var text1 = mutableListOf<String>()
    var n : Int = m
    var n1 : Int = ((n - 100)..n).random()
    var n2 : Int = (0..((n - n1))).random()
    for (j in 0 until n1) {
        var idx: Int = (1..text_.size).random() - 1
        text1.add(text_[idx])
    }
    var text2 = text1
    text2 = random_add(text2, text_)
    text2 = random_remove(text2)
    text2 = random_swap(text2)
    return Pair(text1, text2)
}