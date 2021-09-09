import java.io.File

val mod1 : Int = 998244353
val mod2 : Int = (1e9).toInt() + 7
val alph : Int = 28

/*
 * Захэшруем строки из наборов, чтобы можно было быстро проверять их на равентсво.
 * Вместо самих строк на равенство будем сравнивать по два хэша, вычисленных по разным модулям (много
 * хэшей считать не будем, но использование двух хэшей сильно снизит вероятность коллизий по сравнению
 * с использованием одного).
 */
fun hashes(n : Int, s1 : List <String>) : Array<Array<Long>>{
    var hashof : Array<Array<Long>> = Array(n) {Array (2) {0} }
    for(i in 0 until n){
        var hash1 : Long = 0
        var hash2 : Long = 0
        for(j in 0 until s1[i].length) {
            hash1 = (hash1 * alph + (s1[i][j]).toInt() - ('a').toInt() + 1) % mod1
            hash2 = (hash2 * alph + (s1[i][j]).toInt() - ('a').toInt() + 1) % mod2
        }
        hashof[i][0] = hash1
        hashof[i][1] = hash2
    }
    return hashof
}


fun diff(n : Int, m : Int, text1 : List<String>, text2 : List<String>){
    var first : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} }
    var second : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} } // first[i][j] и second[i][j] служат для восстановления ответа и
    // хранят певрый и второй элемент пары (i1, j1), i1 <= i, j1 <= j, такие что s[i1] = s[j1] и s[i1] - последний элемент, ходящий в
    // наибольшую общую подпоследовательно для префиксов (i, j)
    var dp : Array<Array<Int>> = Array (n + 1) {Array <Int>(m + 1){0} } // dp[i][j] хранит длину наибольшей общей подпоследовательности для префиксов i и j
    var hashfirst = hashes(n, text1) // подсчитываем хэши для первого текста
    var hashsecond = hashes(m, text2) // подсчитываем хэши для второго текста
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(i : Int, j : Int) : Boolean{
        return hashfirst[i][0] == hashsecond[j][0] && hashfirst[i][1] == hashsecond[j][1]
    }
    for(i in 0..n){
        for(j in 0..m){
            if(i != 0 && j != 0 && equals(i - 1, j - 1) && dp[i][j] < dp[i - 1][j - 1] + 1){
                dp[i][j] = dp[i - 1][j - 1] + 1
                first[i][j] = i
                second[i][j] = j
            }
            if(i != 0 && dp[i - 1][j] > dp[i][j]){
                dp[i][j] = dp[i - 1][j]
                first[i][j] = first[i - 1][j]
                second[i][j] = second[i - 1][j]
            }
            if(j != 0 && dp[i][j - 1] > dp[i][j]){
                dp[i][j] = dp[i][j - 1]
                first[i][j] = first[i][j - 1]
                second[i][j] = second[i][j - 1]
            }
        }
    }

}

/*
 * В функции main я считываю содержимое двух файлов в text1 и text2 и вызываю функцию diff.
 */
fun main(args: Array<String>) {
    var n : Int = 0
    var m : Int = 0

    var text1 = mutableListOf<String>()
    var text2 = mutableListOf<String>()
    File(args[0]).useLines {lines -> lines.forEach{text1.add(it)
        n++}}
    File(args[1]).useLines {lines -> lines.forEach{text2.add(it)
    m++}}
    diff(n, m, text1, text2)
}
