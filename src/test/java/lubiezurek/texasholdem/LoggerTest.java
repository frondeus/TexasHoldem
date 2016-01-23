package lubiezurek.texasholdem;

import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 17.12.2015.
 */
public class LoggerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream out;
    private PrintStream err;

    @Before
    public void setUpStreams() {
        this.err = System.err;
        this.out = System.out;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanStreams() {
        System.setOut(out);
        System.setErr(err);
    }

    @org.junit.Test
    public void testStatus() throws Exception {
        Logger.status("Testowy string");
        assertEquals("Testowy string\n", outContent.toString());
    }

    @org.junit.Test
    public void testError() throws Exception {
        Logger.error("Testowy string");
        assertEquals("ERROR!: Testowy string\n", errContent.toString());
    }

    @org.junit.Test
    public void testException() throws Exception {
        Logger.exception(new Exception("Testowy string"));
        assertEquals("EXCEPTION!: java.lang.Exception: Testowy string\n", errContent.toString());
    }
}