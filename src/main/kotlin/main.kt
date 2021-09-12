import java.io.File
import kotlin.math.min

/*
 * Модули для подсчета хэшей
 */
val mod1 : Long = 998244353
val mod2 : Long = (1e9).toLong() + 7
val mod = arrayOf(998244353L, (1e9).toLong() + 7, 16769023L, 1073676287L)
val alph : Long = 10000

/*
 * Захэшруем строки из наборов, чтобы можно было быстро проверять их на равентсво.
 * Вместо самих строк на равенство будем сравнивать по четыре хэша, вычисленных по разным модулям (много
 * хэшей считать не будем, но использование двух хэшей сильно снизит вероятность коллизий по сравнению
 * с использованием одного).
 */

/*
 * Функция, считающая хэши для одной строки
 */
var mass1 = setOf('!', ':', ';', ',', '.', '-')
var mass2 = setOf(':', ';', ',', '-')
fun hash_one_string(s1 : String, flag : Int) : Array <Long>{
    var hash = Array<Long>(4){0L}
    for(j in 0 until s1.length) {
        for(k in 0..3){
            if(flag == 0 || flag == 2) {
                hash[k] = (hash[k] * alph + (s1[j]).toInt() + 1) % mod[k]
            }
            else if(flag == 1)
            {
                var t : Char = s1[j]
                if(t.isUpperCase()){
                    t = t.toLowerCase()
                }
                hash[k] = (hash[k] * alph + t.toInt() + 1) % mod[k]
            }
            else{
                if(s1[j] !in mass2){
                    hash[k] = (hash[k] * alph + (s1[j]).toInt() + 1) % mod[k]
                }
            }
        }
    }
    return hash
}
/*
 * Функция, подсчитывающая хэши для набора строк
 */
fun hashes(n : Int, s1 : List <String>, flag : Int) : Array<Array<Long>>{
    var hashof : Array<Array<Long>> = Array(n) {Array (4) {0L} }
    for(i in 0 until n){
        hashof[i] = hash_one_string(s1[i], flag)
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
 * Следующий класс предназначен для хранения входных данных
 */
data class for_input(var n : Int, var m : Int, var text1 : List<String>, var text2 : List<String>)


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
    var dp : Array<Array<elem>> = Array(n + 1){Array<elem> (m + 1){elem(-1, 0, 0)} }
    dp[0][0] = elem(0, 0, 0)
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
    return dp
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
    var it = 0
    while(_size > 0){
        _size--
        if(answer[_size][0] == '+'){
            println(GREEN+answer[_size]+RESET);
            it++
        }
        else if(answer[_size][0] == '-'){
            println(RED+answer[_size]+RESET);
        }
        else
        {
            println(answer[_size]);
            it++
        }
    }
}

fun diff(data_input : for_input, flag : Int){
    var hashfirst = hashes(data_input.n, data_input.text1, flag) // подсчитываем хэши для первого текста
    var hashsecond = hashes(data_input.m, data_input.text2, flag) // подсчитываем хэши для второго текста
    var cur = calc_dp(data_input.n, data_input.m, hashfirst, hashsecond)
    print_answer(data_input, cur)
}


fun funct(s : String) : String{
    var flag : Boolean = false
    var s1 : String = ""
    var s_list = mutableListOf<Char>()
    for(j in 0..(s.length-1)){
        if(s[j] != ' '){
            if(!flag && s_list.size > 0 && s[j] !in mass1){
                s_list.add(' ')
            }
            else if(flag && s_list.last() in mass1){
                s_list.add(' ')
            }
            flag = true
        }
        else{
            flag = false
        }
        if(flag){
            s_list.add(s[j])
        }
    }
    while(s_list.size > 0 && s_list.last() == ' ')
    {
        s_list.removeLast()
    }
    for(c in s_list){
        s1 += c
    }
    return s1
}
/*
 * В следующей функции я считываю содержимое файлов.
 */
fun input_(args : Array <String>, flag : Int) : for_input?{
    var n : Int = 0
    var m : Int = 0
    var text1 = mutableListOf<String>()
    var text2 = mutableListOf<String>()
    try {
        File(args[0]).useLines { lines ->
            lines.forEach {
                text1.add(it)
                n++
            }
        }
    }
    catch(e: Exception){
        println("Wrong name of file: ${args[0]}")
        return null
    }
    try {
        File(args[1]).useLines { lines ->
            lines.forEach {
                text2.add(it)
                m++
            }
        }
    }
    catch(e: Exception){
        println("Wrong name of file: ${args[1]}")
        return null
    }
    if(flag == 2){
        var new_text1 = mutableListOf<String>()
        var new_text2 = mutableListOf<String>()
        for(lines in text1){
            new_text1.add(funct(lines))
        }
        for(lines in text2){
            new_text2.add(funct(lines))
        }
        text1 = new_text1
        text2 = new_text2
    }
    return for_input(n, m, text1, text2)
}
/*
 * Функция, созданная для проверки корректности работы прогрмаммы.
 * Она возвращает длину наибольшей общей подпоследовательности.
 * И в тестах мы уже проверяем найденную длину наибольшей подпоследовательности с корректной.
 */


fun dp_value(args : Array <String>, flag : Int) : Int{
    var data_input = input_(args, flag)
    if(data_input == null){
        return -1
    }
    var hashfirst = hashes(data_input.n, data_input.text1, flag) // подсчитываем хэши для первого текста
    var hashsecond = hashes(data_input.m, data_input.text2, flag) // подсчитываем хэши для второго текста
    var first : Array<Array<Int>>
    var second : Array<Array<Int>>// first[i][j] и second[i][j] служат для восстановления ответа и
    var cur = calc_dp(data_input.n, data_input.m, hashfirst, hashsecond)
    return cur[data_input.n][data_input.m].value
}
/*
 * В функции main проверяем входные данный на правильность
 */
fun main(args: Array<String>) {
    var Arr : Array<String> = arrayOf("-s", "-lu", "-sp", "-p")
    if(args.size != 3 || !(args[0] in Arr)){
        println("Please, enter the command")
        println("-s  -- print the output of diff on input data")
        println("-lu  -- do not distinguish between lowercase and uppercase letters")
        println("-sp -- delete extra spaces")
        println("-p -- ignore punctuation marks such as ',', '-', ':', ';'")
    }
    else {
        var flag = when(args[0]){
            Arr[0] -> 0
            Arr[1] -> 1
            Arr[2] -> 2
            else -> 3
        }
        var data_input = input_(arrayOf(args[1], args[2]), flag)
        if(data_input != null){
            diff(data_input, flag)
        }
    }
}
