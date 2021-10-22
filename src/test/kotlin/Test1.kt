import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.math.max
import kotlin.math.min
import kotlin.test.*

internal class Test1 {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()
    var end : Int = 0
    /*
     * A function that processes the output of the program and
     * generates data that is transmitted to
     * correctCommands function
     */
    fun getData(file1 : String, file2 : String) : MutableList<MutableList<String>> {
        var text1 = inputOneFile(file1)
        var text2 = inputOneFile(file2)
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
            if(lines.isEmpty()){
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
        /*
         * Function that validates randomly generated tests
         */
        @Test
        fun test6(){
            var t : Int = 10
            while(t > 0){
                t--
                var (text1, text2) = generate() // generate source and destination text
                outputFile(text1, "file9.txt") // write them to files
                outputFile(text2, "file8.txt")
                assertTrue(correctCommands(getData("file9.txt", "file8.txt"))) // run correctCommands on these files
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