import java.io.File
import kotlin.math.min

/*
 * Separate function for counting arrays intended to reconstruct the largest common subsequence
 */

fun calcDp(n : Int, m : Int, hashFirst : IntArray, hashSecond : IntArray) : Array<IntArray>{
    /*
     * The function "equals" compares two strings by comparing the hashes calculated from these strings.
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


fun diff(dataInput : ForInput, flag : Int){
    var hashFirst = hashes(dataInput.n, dataInput.text1, flag) // counting hashes for the first text
    var hashSecond = hashes(dataInput.m, dataInput.text2, flag) // counting hashes for the second text
    var cur = calcDp(dataInput.n, dataInput.m, hashFirst, hashSecond)
    dataInput.printAnswer(cur)
}


/*
 * A function created to check the correctness of the program.
 * It returns the length of the longest common subsequence.
 * And in the tests, we already check the found length of the longest subsequence with the correct one.
 */
fun dpValue(args : Array <String>, flag : Int) : Int{
    var dataInput = InputFiles(args, flag) ?: return -1
    var hashFirst = hashes(dataInput.n, dataInput.text1, flag) // подсчитываем хэши для первого текста
    var hashSecond = hashes(dataInput.m, dataInput.text2, flag) // подсчитываем хэши для второго текста
    var cur = calcDp(dataInput.n, dataInput.m, hashFirst, hashSecond)
    return cur[dataInput.n][dataInput.m]
}
/*
 * In the main function, we check the input data for correctness
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
        var dataInput = inputFiles(arrayOf(args[1], args[2]), flag)
        if(dataInput != null){
            diff(dataInput, flag)
        }
    }
}
