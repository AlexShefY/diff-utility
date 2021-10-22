import java.io.File

/*
 * Outputs text from the list to a file
 */
fun outputFile(text : MutableList<String>, file : String){
    File(file).printWriter().use { out ->
        text.forEach {
            out.println(it)
        }
    }
}