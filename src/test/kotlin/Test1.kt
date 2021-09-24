import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.math.max
import kotlin.math.min
import kotlin.test.*


val m : Int = 10000
internal class Test1 {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()
    var end : Int = 0
    /*
     * Функция , которая обрабатывает вывод программы и
     * формирует данные, которые передаются в
     * функцию correctCommands
     */
    fun getData(file1 : String, file2 : String) : MutableList<MutableList<String>> {
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
        main(arrayOf("-s", file1, file2))
        var j = 0
        var j1 = 0
        var p1 = 0
        var commands = mutableListOf<String>()
        for(lines in stream.toString().trim().lines()){
            if(p1 < end){
                p1++
                continue
            }
            p1++
            commands.add(lines)
            if(lines.length == 0){
                end++
                continue
            }
        }
        end = p1
        return mutableListOf(text1, text2, commands)
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
            assertEquals(16, dpValue(arrayOf("file1.txt", "file2.txt"), 0))
            assertEquals(7, dpValue(arrayOf("file3.txt", "file4.txt"), 0))
            assertEquals(16, dpValue(arrayOf("file5.txt", "file6.txt"), 0))
        }
        @Test
        fun test2() {
            assertTrue(correctCommands(getData("file1.txt", "file2.txt")))
            end = 0
        }
        @Test
        fun test3() {
            assertTrue(correctCommands(getData("file3.txt", "file4.txt")))
            end = 0
        }
        @Test
        fun test4(){
            assertTrue(correctCommands(getData("file5.txt", "file6.txt")))
            end = 0
        }
        @Test
        fun test6(){
            var t : Int = 10
            while(t > 0){
                t--
                var text_ = mutableListOf<String>()
                File("file7.txt").useLines { lines ->
                    lines.forEach {
                        text_.add(it)
                    }
                }
                var n : Int = m
                var n1 : Int = ((n - 100)..n).random()
                var n2 : Int = (0..((n - n1))).random()
                var text1 = mutableListOf<String>()
                var text_add = mutableListOf<String>()
                for(j in 0 until n1){
                    var idx : Int = (1..text_.size).random() - 1
                    text1.add(text_[idx])
                }

                File("file9.txt").printWriter().use { out ->
                    text1.forEach {
                        out.println("$it")
                    }
                }
                var text2 = text1
                var m_add = (0..m).random()
                while(m_add > 0 && text_add.size > 0){
                    var idx1 = (0..text2.size).random()
                    var idx2 = (text_add.indices).random()
                    text2.add(idx1, text_add[idx2])
                    m_add--
                }
                var m_del = min(max(0, text2.size - 8000), m) / 2
                m_del = (0..m_del).random()
                while(m_del > 0){
                    text2.removeAt((text2.indices).random())
                    m_del--
                }
                var m_swap = (0..m).random()
                while(m_swap > 0 && text2.size > 0){
                    var idx1 = (text2.indices).random()
                    var idx2 = (text2.indices).random()
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
                assertTrue(correctCommands(getData("file9.txt", "file8.txt")))
            }
            end = 0
        }
        @Test
        fun test7(){
            assertTrue(correctCommands(getData("file9.txt", "file8.txt")))
            end = 0
        }
        @Test
        fun test_hashes(){
            assertTrue(hashOneString("a", 0) != hashOneString("b", 0))
            assertEquals(hashOneString("akhdkajdhshkfjd", 0), hashOneString("akhdkajdhshkfjd", 0))
            assertTrue(hashOneString("aab", 0) != hashOneString("baa", 0))
            assertTrue(hashOneString("abc\n", 0) != hashOneString("abc", 0))
            assertTrue(hashOneString("abc ", 0) != hashOneString("abc", 0))
            assertEquals(hashOneString("объёма информации.", 0), hashOneString("объёма информации.", 0))
            assertTrue(hashOneString("Это важное замечание!", 0) != hashOneString("Это важное\\замечание!", 0))
        }
 }