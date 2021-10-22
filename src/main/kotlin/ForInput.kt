/*
 * Colors to tint the result
 */
const val RESET : String = "\u001B[0m";
const val RED : String = "\u001B[31m";
const val GREEN : String = "\u001B[32m";
/*
 * The next class is for storing input data
 */
data class ForInput(var n : Int, var m : Int, var text1 : List<String>, var text2 : List<String>){
    /*
     * Reconstructs the response based on dynamics. Displays line-by-line added, set and deleted lines
     */
    fun printAnswer(dp : Array<IntArray>){
        var answer = mutableListOf<String>()
        var from = n
        var to = m
        var size = 0
        while(from > 0 || to > 0){
            while(to > 0 && dp[from][to - 1] == dp[from][to]){
                size++
                to--
                answer.add("+${text2[to]}")
            }
            while(from > 0 && dp[from - 1][to] == dp[from][to]){
                from--
                size++
                answer.add("-${text1[from]}")
            }
            if(from != 0 && to != 0) {
                size++
                answer.add("=${text1[from - 1]}")
                from--
                to--
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
}