import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*
import java.io.File

internal class Test1 {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()
    /*
     * Функция , проверяющая вывод программы, чтобы при выполнении
     * выведенных команд из исходного текста мы получили конечный
     */
    fun correct(file1 : String, file2 : String) : Boolean{

        var text0 = mutableListOf<String>()
        var text1 = mutableListOf<String>()
        File(file1).useLines { lines ->
            lines.forEach {
                text1.add(it)
            }
        }
        var text2 = mutableListOf<String>()
        File(file2).useLines { lines ->
            lines.forEach {
                text2.add(it)
            }
        }
        main(arrayOf(file1, file2))
        var j = 0
        for(lines in stream.toString().trim().lines()){
            if(lines.length == 0){
                break
            }
            print(lines)
            var s1 : String = ""
            var t : Int = 0
            while(t < lines.length && lines[t] != '+' && lines[t] != '='){
                t++
            }
            if(lines[t] == '+'){
                t++
            }
            var t1  = t + 1
            while(t1 < lines.length && lines[t1] != '+') {
                t1++
            }
            for(p in t + 1 until t1){
                s1 += lines[p]
            }
            if(lines[t] == '+'){
                text0.add(s1)
            }
            else if(lines[t] == '-'){
                j++
            }
            else if(lines[t] == '=')
            {
                if(j == text1.size){
                    return false
                }
                text0.add(text1[j])
                j++
            }
        }
        return text0 == text2
    }
    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    @Test
    fun test1() {
        assertEquals(16, dp_value(arrayOf("file1.txt", "file2.txt")))
        assertEquals(7, dp_value(arrayOf("file3.txt", "file4.txt")))
        assertEquals(16, dp_value(arrayOf("file5.txt", "file6.txt")))
    }

    @Test
    fun test2() {
        assert(correct("file1.txt", "file2.txt"))
    }
    @Test
    fun test3() {
        assert(correct("file3.txt", "file4.txt"))
    }
    @Test
    fun test4(){
        assert(correct("file5.txt", "file6.txt"))
    }

    @Test
    fun test_hashes(){
        assert(!check_hashes_equals(hash_one_string("a"), hash_one_string("b")))
        assert(check_hashes_equals(hash_one_string("akhdkajdhshkfjd"), hash_one_string("akhdkajdhshkfjd")))
        assert(!check_hashes_equals(hash_one_string("aab"), hash_one_string("baa")))
        assert(!check_hashes_equals(hash_one_string("abc\n"), hash_one_string("abc")))
        assert(!check_hashes_equals(hash_one_string("abc "), hash_one_string("abc")))
        assert(check_hashes_equals(hash_one_string("объёма информации."), hash_one_string("объёма информации.")))
        assert(!check_hashes_equals(hash_one_string("Это важное замечание!"), hash_one_string("Это важное\\замечание!")))
    }
}
