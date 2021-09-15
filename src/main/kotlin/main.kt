import java.io.File
import kotlin.math.min
/*
 * Модули для подсчета хэшей
 */
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
/*
 * Создал класс для хэшей и переопределил оператор равно
 */
class Hash(var arr : Array<Long> = arrayOf(0L, 0L, 0L, 0L)){
    fun add(sym : Char){
        for(i in 0..3){
            arr[i] = (arr[i] * alph + sym.code + 1) % mod[i]
        }
    }
    override operator fun equals(other : Any?): Boolean{
        if(other !is Hash){
            return false
        }
        for(i in 0..3){
            if(this.arr[i] != other.arr[i]){
                return false
            }
        }
        return true
    }
}
fun hashOneString(s1 : String, flag : Int) : Hash{
    var hash = Hash(arrayOf(0L, 0L, 0L, 0L))
    for(j in s1.indices) {
            if(flag == 0 || flag == 2) {
                hash.add(s1[j])
            }
            else if(flag == 1)
            {
                var t : Char = s1[j]
                if(t.isUpperCase()){
                    t = t.toLowerCase()
                }
                hash.add(t)
            }
            else{
                if(s1[j] !in mass2){
                    hash.add(s1[j])
                }
            }
    }
    return hash
}
/*
 * Функция, подсчитывающая хэши для набора строк
 */
fun hashes(n : Int, s1 : List <String>, flag : Int) : Array<Hash>{
    var hashof : Array<Hash> = Array(n) {Hash()}
    for(i in 0 until n){
        hashof[i] = hashOneString(s1[i], flag)
    }
    return hashof
}
/*
 * Следующий класс предназначен для хранения входных данных
 */
data class ForInput(var n : Int, var m : Int, var text1 : List<String>, var text2 : List<String>)
// Класс предназначенный для хранения значения динамики и данных, необходимых для восстановления ответа.
data class Elem(var value : Int, var i_pred : Int, var j_pred : Int)
/*
 * Отдельная функция для подсчета массивов, предназначенных для восстановления наибольшей общей подпоследовательности
 */

fun calcDp(n : Int, m : Int, hashFirst : Array<Hash>, hashSecond : Array<Hash>) : Array<Array<Elem> >{
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(i : Int, j : Int) : Boolean{
        return hashFirst[i] == hashSecond[j]
    }
    var dp : Array<Array<Elem>> = Array(n + 1){Array<Elem> (m + 1){Elem(-1, 0, 0)} }
    dp[0][0] = Elem(0, 0, 0)
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
fun printAnswer(dataInput : ForInput, dp : Array<Array<Elem>>){
    var answer = mutableListOf<String>()
    var i = dataInput.n
    var j = dataInput.m
    var size = 0
    while(i > 0 || j > 0){
        var i1 = dp[i][j].i_pred
        var j1 = dp[i][j].j_pred
        var t = j
        while(t > j1){
            size++
            answer.add("+${dataInput.text2[t - 1]}")
            t--
        }
        t = i
        while(t > i1) {
            size++
            answer.add("-${dataInput.text1[t - 1]}")
            t--
        }
        if(i1 != 0) {
            size++
            answer.add("=${dataInput.text1[i1 - 1]}")
        }
        i = i1 - 1
        j = j1 - 1
    }
    var it = 0
    while(size > 0){
        size--
        if(answer[size][0] == '+'){
            println(GREEN+answer[size]+RESET);
            it++
        }
        else if(answer[size][0] == '-'){
            println(RED+answer[size]+RESET);
        }
        else
        {
            println(answer[size]);
            it++
        }
    }
}

fun diff(dataInput : ForInput, flag : Int){
    var hashfirst = hashes(dataInput.n, dataInput.text1, flag) // подсчитываем хэши для первого текста
    var hashsecond = hashes(dataInput.m, dataInput.text2, flag) // подсчитываем хэши для второго текста
    var cur = calcDp(dataInput.n, dataInput.m, hashfirst, hashsecond)
    printAnswer(dataInput, cur)
}


fun spaces(s : String) : String{
    var flag : Boolean = false
    var s1 : String = ""
    var sList = mutableListOf<Char>()
    for(j in s.indices){
        if(s[j] != ' '){
            if(!flag && sList.size > 0 && s[j] !in mass1){
                sList.add(' ')
            }
            else if(flag && sList.last() in mass1){
                sList.add(' ')
            }
            flag = true
        }
        else{
            flag = false
        }
        if(flag){
            sList.add(s[j])
        }
    }
    while(sList.size > 0 && sList.last() == ' ')
    {
        sList.removeLast()
    }
    for(c in sList){
        s1 += c
    }
    return s1
}
/*
 * В следующей функции я считываю содержимое файлов.
*/
fun input(args : Array <String>, flag : Int) : ForInput?{
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
        var newText1 = mutableListOf<String>()
        var newText2 = mutableListOf<String>()
        for(lines in text1){
            newText1.add(spaces(lines))
        }
        for(lines in text2){
            newText2.add(spaces(lines))
        }
        text1 = newText1
        text2 = newText2
    }
    return ForInput(n, m, text1, text2)
}
/*
 * Функция, созданная для проверки корректности работы прогрмаммы.
 * Она возвращает длину наибольшей общей подпоследовательности.
 * И в тестах мы уже проверяем найденную длину наибольшей подпоследовательности с корректной.
 */

fun dpValue(args : Array <String>, flag : Int) : Int{
    var dataInput = input(args, flag) ?: return -1
    var hashFirst = hashes(dataInput.n, dataInput.text1, flag) // подсчитываем хэши для первого текста
    var hashSecond = hashes(dataInput.m, dataInput.text2, flag) // подсчитываем хэши для второго текста
    var cur = calcDp(dataInput.n, dataInput.m, hashFirst, hashSecond)
    return cur[dataInput.n][dataInput.m].value
}
/*
 * В функции main проверяем входные данный на правильность
 */
fun main(args: Array<String>) {
    var arr : Array<String> = arrayOf("-s", "-lu", "-sp", "-p")
    if(args.size != 3 || args[0] !in arr){
        println("Please, enter the command")
        println("-s  -- print the output of diff on input data")
        println("-lu  -- do not distinguish between lowercase and uppercase letters")
        println("-sp -- delete extra spaces")
        println("-p -- ignore punctuation marks such as ',', '-', ':', ';'")
    }
    else {
        var flag = when(args[0]){
            arr[0] -> 0
            arr[1] -> 1
            arr[2] -> 2
            else -> 3
        }
        var dataInput = input(arrayOf(args[1], args[2]), flag)
        if(dataInput != null){
            diff(dataInput, flag)
        }
    }
}