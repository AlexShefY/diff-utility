/*
 * A function that checks the output of a program so that when executed
 * commands output from the source text, we got the final
*/
fun correctCommands(data : MutableList<MutableList<String>>) : Boolean{
    var text1 : MutableList<String> = data[0]
    var text2 : MutableList<String> = data[1]
    var commands : MutableList<String> = data[2]
    var j = 0
    var j1 = 0
    var text0 = mutableListOf<String>()
    for(command in commands){
        var s1 = ""
        var t : Int = 0
        while(t < command.length && command[t] != '+' && command[t] != '=' && command[t] != '-'){
            t++
        }
        var t1 = 0
        if(command[t] == '='){
            t1 = command.length
        }
        else{
            t1 = command.length - 4
        }
        if(t + 1 < t1) {
            var p = t + 1
            while(p < t1){
                s1 += command[p]
                p++
            }
        }
        if(command[t] == '+'){
            text0.add(s1)
            j1++
        }
        else if(command[t] == '-'){
            j++
        }
        else if(command[t] == '=')
        {
            text0.add(text1[j])
            j++
            j1++
        }
    }
    return text0 == text2
}
