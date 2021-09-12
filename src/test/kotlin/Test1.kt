import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*
import java.io.File
import kotlin.math.min

val m : Int = 100
internal class Test1 {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()
    var end : Int = 0
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
                end++
                continue
            }
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
            if(t + 1 < t1) {
                for (p in (t + 1) until t1) {
                    s1 += lines[p]
                }
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
            end++
        }
        j = 0
        while(j < 42 && j < text0.size &&j < text2.size)
        {
            if(text0[j] != text2[j]){
                return false
            }
            j++
        }
        return true
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
        end = 0
    }
    @Test
    fun test3() {
        assert(correct("file3.txt", "file4.txt"))
        end = 0
    }
    @Test
    fun test4(){
        assert(correct("file5.txt", "file6.txt"))
        end = 0
    }
    @Test
    fun test6(){
        var t : Int = 1
        while(t > 0){
            t--
            var text_ = mutableListOf<String>()
        File("file7.txt").useLines { lines ->
            lines.forEach {
                text_.add(it)
            }
        }
        var n : Int = text_.size
        var n1 : Int = (1..n).random()
        var n2 : Int = (0..(n - n1)).random()
        var text1 = mutableListOf<String>()
        var text_add = mutableListOf<String>()
        for(j in 0 until n1){
            var idx : Int = (1..n1).random() - 1
            text1.add(text_[idx])
        }

        File("file9.txt").printWriter().use { out ->
            text1.forEach {
                out.println("$it")
            }
        }
        var text2 = text1
        text2 = text1
        var m_add = (0..m).random()
        while(m_add > 0 && text_add.size > 0){
            var idx1 = (0..text2.size).random()
            var idx2 = (0..(text_add.size - 1)).random()
            text2.add(idx1, text_add[idx2])
            m_add--
        }
        var m_del = min(text2.size, m)
        m_del = (0..m_del).random()
        while(m_del > 0){
            text2.removeAt((0..(text2.size - 1)).random())
            m_del--
        }
        var m_swap = (0..m).random()
        while(m_swap > 0){
            var idx1 = (0..(text2.size - 1)).random()
            var idx2 = (0..(text2.size - 1)).random()
            var c = text2[idx2]
            text2[idx2] = text2[idx1]
            text2[idx1] = c
            m_swap--
        }
        File("file8.txt").printWriter().use { out ->
            text2.forEach {
                out.println("$it")
            }
        }
        assert(correct("file9.txt", "file8.txt"))
        }
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
