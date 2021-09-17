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
/*
 * Функция, считающая хэши для одной строки
 */
fun hashOneString(s1 : String, flag : Int) : Hash{
    var hash = Hash(arrayOf(0L, 0L, 0L, 0L))
    s1.forEach { ch ->
            if(flag == 0 || flag == 2) {
                hash.add(ch)
            }
            else if(flag == 1)
            {
                var t : Char = ch
                if(t.isUpperCase()){
                    t = t.lowercaseChar()
                }
                hash.add(t)
            }
            else{
                if(ch !in mass2){
                    hash.add(ch)
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
/*
 * Отдельная функция для подсчета массивов, предназначенных для восстановления наибольшей общей подпоследовательности
 */

fun calcDp(n : Int, m : Int, hashFirst : Array<Hash>, hashSecond : Array<Hash>) : Array<IntArray>{
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(i : Int, j : Int) : Boolean{
        return hashFirst[i] == hashSecond[j]
    }
    var dp : Array<IntArray> = Array(n + 1){IntArray (m + 1) {0}}
    dp[0][0] = 0
    for(i in 0..n){
        for(j in 0..m){
            if(i != 0 && j != 0 && equals(i - 1, j - 1) && dp[i][j] < dp[i - 1][j - 1] + 1){
                dp[i][j] = dp[i - 1][j - 1] + 1
            }
            if(i != 0 && dp[i - 1][j] > dp[i][j]){
                dp[i][j] = dp[i - 1][j]
            }
            if(j != 0 && dp[i][j - 1] > dp[i][j]){
                dp[i][j] = dp[i][j - 1]
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
fun printAnswer(dataInput : ForInput, dp : Array<IntArray>){
    var answer = mutableListOf<String>()
    var i = dataInput.n
    var j = dataInput.m
    var size = 0
    while(i > 0 || j > 0){
        while(j > 0 && dp[i][j - 1] == dp[i][j]){
            size++
            j--
            answer.add("+${dataInput.text2[j]}")
        }
        while(i > 0 && dp[i - 1][j] == dp[i][j]){
            i--
            size++
            answer.add("-${dataInput.text1[i]}")
        }
        if(i != 0 && j != 0) {
            size++
            answer.add("=${dataInput.text1[i - 1]}")
            i--
            j--
        }
    }
    answer.reverse()
    answer.forEach{ str ->
        if(str[0] == '+'){
            println(GREEN+str+RESET);
        }
        else if(str[0] == '-'){
            println(RED+str+RESET);
        }
        else
        {
            println(str);
        }
    }
}

fun diff(dataInput : ForInput, flag : Int){
    var hashFirst = hashes(dataInput.n, dataInput.text1, flag) // подсчитываем хэши для первого текста
    var hashSecond = hashes(dataInput.m, dataInput.text2, flag) // подсчитываем хэши для второго текста
    var cur = calcDp(dataInput.n, dataInput.m, hashFirst, hashSecond)
    printAnswer(dataInput, cur)
}


fun spaces(s : String) : String{
    var flag : Boolean = false
    var sList = mutableListOf<Char>()
    s.forEach{ ch ->
        if(ch != ' '){
            if(!flag && sList.size > 0 && ch !in mass1){
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
            sList.add(ch)
        }
    }
    var sList1 = sList.dropWhile({it == ' '})
    return sList1.joinToString("")
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
    return cur[dataInput.n][dataInput.m]
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