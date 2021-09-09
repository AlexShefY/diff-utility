import java.io.File

/*
 * Модули для подсчета хэшей
 */
val mod1 : Int = 998244353
val mod2 : Int = (1e9).toInt() + 7
val mod = arrayOf(998244353, (1e9).toInt() + 7, 16769023, 1073676287)
val alph : Int = 1200

/*
 * Захэшруем строки из наборов, чтобы можно было быстро проверять их на равентсво.
 * Вместо самих строк на равенство будем сравнивать по два хэша, вычисленных по разным модулям (много
 * хэшей считать не будем, но использование двух хэшей сильно снизит вероятность коллизий по сравнению
 * с использованием одного).
 */

/*
 * Функция, считающая хэши для одной строки
 */
fun hash_one_string(s1 : String) : Array <Long>{
    var hash = Array<Long>(4){0L}
    for(j in 0 until s1.length) {
        for(k in 0..3){
            hash[k] = (hash[k] * alph + (s1[j]).toInt() + 1) % mod[k]
        }
    }
    return hash
}
/*
 * Функция, подсчитывающая хэши для набора строк
 */
fun hashes(n : Int, s1 : List <String>) : Array<Array<Long>>{
    var hashof : Array<Array<Long>> = Array(n) {Array (4) {0} }
    for(i in 0 until n){
        hashof[i] = hash_one_string(s1[i])
    }
    return hashof
}
/*
 * Функция, определяющая по подсчитанным хэшам для двух строк равны ли эти строки
 */
fun check_hashes_equals(a : Array<Long>, b : Array<Long>) : Boolean
{
    for(i in 0..3){
        if(a[i] != b[i]){
            return false
        }
    }
    return true
}

/*
 * Отдельная функция для подсчета массивов, предназначенных для восстановления наибольшей общей подпоследовательности
 */
fun calc_dp(n : Int, m : Int, hashfirst : Array<Array<Long>>, hashsecond : Array<Array<Long>>) : Array<Array<Array<Int> > >{
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(i : Int, j : Int) : Boolean{
        return check_hashes_equals(hashfirst[i], hashsecond[j])
    }
    var first : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} }
    var second : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} } // first[i][j] и second[i][j] служат для восстановления ответа и
    // хранят певрый и второй элемент пары (i1, j1), i1 <= i, j1 <= j, такие что s[i1] = s[j1] и s[i1] - последний элемент, ходящий в
    // наибольшую общую подпоследовательно для префиксов (i, j)
    var dp : Array<Array<Int>> = Array (n + 1) {Array <Int>(m + 1){0} } // dp[i][j] хранит длину наибольшей общей подпоследовательности для префиксов i и j

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
    return arrayOf(first, second)
}

/*
 * Цвета для подкрашивания результата
 */
val RESET : String = "\u001B[0m";
val RED : String = "\u001B[31m";
var GREEN : String = "\u001B[32m";


/*
 * Восстанавливает ответ по динамике. Выводит построчно добавленные, неизмененные и удаленные строки
 */
fun print_answer(n : Int, m : Int, text1 : List<String>, text2 : List<String>, first : Array<Array<Int>>, second : Array<Array<Int>>){
    var answer = mutableListOf<String>()
    var i = n
    var j = m
    var _size = 0
    while(i > 0 || j > 0){
        println("$i $j")
        var i1 = first[i][j]
        var j1 = second[i][j]
        var t = j
        while(t > j1){
            _size++
            answer.add("+${text2[t - 1]}")
            t--
        }
        t = i
        while(t > i1) {
            _size++
            answer.add("-${text1[t - 1]}")
            t--
        }
        if(i1 != 0) {
            _size++
            answer.add("=${text1[i1 - 1]}")
        }
        i = i1 - 1
        j = j1 - 1
    }
    while(_size > 0){
        _size--
        if(answer[_size][0] == '+'){
            println(GREEN + "${answer[_size]}" + RESET);
        }
        else if(answer[_size][0] == '-'){
            println(RED + "${answer[_size]}" + RESET);
        }
        else
        {
            println("${answer[_size]}");
        }
    }
}

fun diff(n : Int, m : Int, text1 : List<String>, text2 : List<String>){
    var hashfirst = hashes(n, text1) // подсчитываем хэши для первого текста
    var hashsecond = hashes(m, text2) // подсчитываем хэши для второго текста
    var first : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} }
    var second : Array<Array<Int>> = Array  (n + 1) {Array <Int>(m + 1){0} } // first[i][j] и second[i][j] служат для восстановления ответа и
    var cur = calc_dp(n, m, hashfirst, hashsecond)
    first = cur[0]
    second = cur[1]
    print_answer(n, m, text1, text2, first, second)
}

/*
 * В функции main я считываю содержимое двух файлов в text1 и text2 и вызываю функцию diff.
 */
fun main(args: Array<String>) {
    var n : Int = 0
    var m : Int = 0
    var text1 = mutableListOf<String>()
    var text2 = mutableListOf<String>()
    File("file1.txt").useLines {lines -> lines.forEach{text1.add(it)
        n++}}
    File("file2.txt").useLines {lines -> lines.forEach{text2.add(it)
    m++}}
    diff(n, m, text1, text2)
}
