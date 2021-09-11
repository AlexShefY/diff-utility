import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*

internal class Test1 {

    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()
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
