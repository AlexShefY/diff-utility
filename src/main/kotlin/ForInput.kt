/*
 * Цвета для подкрашивания результата
 */
val RESET : String = "\u001B[0m";
val RED : String = "\u001B[31m";
var GREEN : String = "\u001B[32m";
/*
 * Следующий класс предназначен для хранения входных данных
 */
data class ForInput(var n : Int, var m : Int, var text1 : List<String>, var text2 : List<String>){
    /*
     * Восстанавливает ответ по динамике. Выводит построчно добавленные, неизмененные и удаленные строки
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