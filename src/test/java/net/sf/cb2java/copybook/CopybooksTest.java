package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import junit.framework.TestCase;

import static net.sf.cb2java.copybook.Copybooks.*;

/**
 *
 * @author jevery
 */
public class CopybooksTest extends TestCase {
    
    public CopybooksTest(String testName) {
        super(testName);
    }

    /**
     * Test of readCopybooks method, of class Copybooks.
     */
    /*public void testReadCopybooks_List() throws Exception {
        Map<String, Copybook> copybooks = readCopybooks(new File("./target/test-classes/").listFiles());
        assertTrue(copybooks.containsKey("a"));
        assertTrue(copybooks.containsKey("b"));
    }*/

    /**
     * Test of readCopybooks method, of class Copybooks.
     */
    public void testReadCopybooks_FileArr() throws Exception {
        Map<String, Copybook> copybooks = readCopybooks(new File("./target/test-classes/").listFiles(new FilenameFilter() {
            public boolean accept(File file, String filename) {
                return filename.endsWith(".copybook");
            }
        }));
        assertEquals(3, copybooks.size());
        assertTrue(copybooks.containsKey("a"));
        assertTrue(copybooks.containsKey("b"));
    }

    /**
     * Test of copybookNameOfFile method, of class Copybooks.
     */
    public void testCopybookNameOfFile() {
        assertEquals("cpy", copybookNameOfFile(new File("CPY.txt")));
    }
}
