import java.io.File

/*
 * Выводит текст из списка в файл
 */
fun outputFile(text : MutableList<String>, file : String){
    File(file).printWriter().use { out ->
        text.forEach {
            out.println(it)
        }
    }
}