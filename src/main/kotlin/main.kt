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
// Класс предназначенный для хранения значения динамики и данных, необходимых для восстановления ответа.
data class elem(var value : Int, var i_pred : Int, var j_pred : Int)
/*
 * Отдельная функция для подсчета массивов, предназначенных для восстановления наибольшей общей подпоследовательности
 */
fun calc_dp(n : Int, m : Int, hashfirst : Array<Array<Long>>, hashsecond : Array<Array<Long>>) : Array<Array<elem> >{
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(i : Int, j : Int) : Boolean{
        return check_hashes_equals(hashfirst[i], hashsecond[j])
    }
    var dp : Array<Array<elem>> = Array(n + 1){Array<elem> (m + 1){elem(0, 0, 0)} }
    for(i in 0..n){
        for(j in 0..m){
            if(i != 0 && j != 0 && equals(i - 1, j - 1) && dp[i][j].value < dp[i - 1][j - 1].value + 1){
                dp[i][j].value = dp[i - 1][j - 1].value + 1
                dp[i][j].i_pred = i
                dp[i][j].j_pred = j
            }
            if(i != 0 && dp[i - 1][j].value > dp[i][j].value){
                dp[i][j].value = dp[i - 1][j].value
                dp[i][j].i_pred = dp[i - 1][j].i_pred
                dp[i][j].j_pred = dp[i - 1][j].j_pred
            }
            if(j != 0 && dp[i][j - 1].value > dp[i][j].value){
                dp[i][j].value = dp[i][j - 1].value
                dp[i][j].i_pred = dp[i][j - 1].i_pred
                dp[i][j].j_pred = dp[i][j - 1].j_pred
            }
        }
    }
    println(dp[n][m].value)
    return dp
}

/*
 * Цвета для подкрашивания результата
 */
val RESET : String = "\u001B[0m";
val RED : String = "\u001B[31m";
var GREEN : String = "\u001B[32m";



/*
 * Следующий класс предназначен для хранения входных данных
 */
data class for_input(var n : Int, var m : Int, var text1 : List<String>, var text2 : List<String>)


/*
 * Восстанавливает ответ по динамике. Выводит построчно добавленные, неизмененные и удаленные строки
 */
fun print_answer(data_input : for_input, dp : Array<Array<elem>>){
    var answer = mutableListOf<String>()
    var i = data_input.n
    var j = data_input.m
    var _size = 0
    while(i > 0 || j > 0){
        var i1 = dp[i][j].i_pred
        var j1 = dp[i][j].j_pred
        var t = j
        while(t > j1){
            _size++
            answer.add("+${data_input.text2[t - 1]}")
            t--
        }
        t = i
        while(t > i1) {
            _size++
            answer.add("-${data_input.text1[t - 1]}")
            t--
        }
        if(i1 != 0) {
            _size++
            answer.add("=${data_input.text1[i1 - 1]}")
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

fun diff(data_input : for_input){
    var hashfirst = hashes(data_input.n, data_input.text1) // подсчитываем хэши для первого текста
    var hashsecond = hashes(data_input.m, data_input.text2) // подсчитываем хэши для второго текста
    var first : Array<Array<Int>>
    var second : Array<Array<Int>>// first[i][j] и second[i][j] служат для восстановления ответа и
    var cur = calc_dp(data_input.n, data_input.m, hashfirst, hashsecond)
    print_answer(data_input, cur)
}

/*
 * В следующей функции я считываю содержимое файлов.
 */
fun input_(args : Array <String>) : for_input{
    var n : Int = 0
    var m : Int = 0
    var text1 = mutableListOf<String>()
    var text2 = mutableListOf<String>()
    File(args[0]).useLines {lines -> lines.forEach{text1.add(it)
        n++}}
    File(args[1]).useLines {lines -> lines.forEach{text2.add(it)
        m++}}
    return for_input(n, m, text1, text2)
}
/*
 * Функция, созданная для проверки корректности работы прогрмаммы.
 * Она возвращает длину наибольшей общей подпоследовательности.
 * И в тестах мы уже проверяем найденную длину наибольшей подпоследовательности с корректной.
 */

fun dp_value(args : Array <String>) : Int{
    var data_input = input_(args)
    var hashfirst = hashes(data_input.n, data_input.text1) // подсчитываем хэши для первого текста
    var hashsecond = hashes(data_input.m, data_input.text2) // подсчитываем хэши для второго текста
    var first : Array<Array<Int>>
    var second : Array<Array<Int>>// first[i][j] и second[i][j] служат для восстановления ответа и
    var cur = calc_dp(data_input.n, data_input.m, hashfirst, hashsecond)
    return cur[data_input.n][data_input.m].value
}
fun main(args: Array<String>) {
    var data_input = input_(args)
    diff(data_input)
}
