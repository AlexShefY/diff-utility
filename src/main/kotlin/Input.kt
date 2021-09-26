import java.io.File

/*
 * В следующей функции я считываю содержимое файлов.
 * Обрабатываем ошибки на случай, если пользователь
 * неправильно введет имя файла
*/
fun InputFiles (args : Array <String>, flag : Int) : ForInput?{
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
 * Считывание содержимого одного файла
 */

fun inputOneFile(file : String) : MutableList<String>{
    var text = mutableListOf<String>()
    File(file).useLines { lines ->
        lines.forEach {
            text.add(it)
        }
    }
    return text
}