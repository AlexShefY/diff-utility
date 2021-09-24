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
 * Функция, считающая хэш для одной строки
 */
fun hashOneString(s1 : String, flag : Int) : Int{
    var res = ""
    s1.forEach { ch ->
            if(flag == 0 || flag == 2) {
                res += ch
            }
            else if(flag == 1)
            {
                var t : Char = ch
                if(t.isUpperCase()){
                    t = t.lowercaseChar()
                }
                res += t
            }
            else{
                if(ch !in mass2){
                    res += ch
                }
            }
    }
    return res.hashCode()
}
/*
 * Функция, подсчитывающая хэши для набора строк
 */
fun hashes(n : Int, s1 : List <String>, flag : Int) : IntArray{
    var hashOf = IntArray(n) {0}
    for(i in 0 until n){
        hashOf[i] = hashOneString(s1[i], flag)
    }
    return hashOf
}

/*
 * Отдельная функция для подсчета массивов, предназначенных для восстановления наибольшей общей подпоследовательности
 */

fun calcDp(n : Int, m : Int, hashFirst : IntArray, hashSecond : IntArray) : Array<IntArray>{
    /*
     * Функция equals сравнивает две строки сравнением хэшей, насчитанных по этим строкам.
     */
    fun equals(from : Int, to : Int) : Boolean{
        return hashFirst[from] == hashSecond[to]
    }
    var dp : Array<IntArray> = Array(n + 1){IntArray (m + 1) {0}}
    dp[0][0] = 0
    for(from in 0..n){
        for(to in 0..m){
            if(from != 0 && to != 0 && equals(from - 1, to - 1) && dp[from][to] < dp[from - 1][to - 1] + 1){
                dp[from][to] = dp[from - 1][to - 1] + 1
            }
            if(from != 0 && dp[from - 1][to] > dp[from][to]){
                dp[from][to] = dp[from - 1][to]
            }
            if(to != 0 && dp[from][to - 1] > dp[from][to]){
                dp[from][to] = dp[from][to - 1]
            }
        }
    }
    return dp
}


/*
 * Нормируем строки по пробелам
 */
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

fun diff(dataInput : ForInput, flag : Int){
    var hashFirst = hashes(dataInput.n, dataInput.text1, flag) // подсчитываем хэши для первого текста
    var hashSecond = hashes(dataInput.m, dataInput.text2, flag) // подсчитываем хэши для второго текста
    var cur = calcDp(dataInput.n, dataInput.m, hashFirst, hashSecond)
    dataInput.printAnswer(cur)
}


/*
 * Функция, созданная для проверки корректности работы прогрмаммы.
 * Она возвращает длину наибольшей общей подпоследовательности.
 * И в тестах мы уже проверяем найденную длину наибольшей подпоследовательности с корректной.
 */
fun dpValue(args : Array <String>, flag : Int) : Int{
    var dataInput = InputFiles(args, flag) ?: return -1
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
        var dataInput = InputFiles(arrayOf(args[1], args[2]), flag)
        if(dataInput != null){
            diff(dataInput, flag)
        }
    }
}
