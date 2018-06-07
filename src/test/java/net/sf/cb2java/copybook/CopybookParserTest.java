package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.IntegerData;
import net.sf.cb2java.data.Record;

/**
 *
 * @author devstopfix
 */
public class CopybookParserTest extends TestCase {
    
    public CopybookParserTest(String testName) {
        super(testName);
    }

    /**
     * Take a copybook and parse it.
     *
     * @throws FileNotFoundException
     */
    public void testWeCanParseCopybook() throws FileNotFoundException {
        Copybook copybook = CopybookParser.parse("A", new FileInputStream(new File("./target/test-classes/a.copybook")));
        assertEquals(55, copybook.getLength());
    }
    
    /**
     * Parse copybook data.
     *
     * @throws FileNotFoundException
     */
    public void testWeCanParseCopybookData() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        assertEquals(1, results.size());
        Record record = results.get(0);
        Data root = record.getChild("ROOT");
        assertEquals("ABCDEF", root.getChildren().get(0).toString());
        assertEquals("B", ((Data)root.getChildren().get(1)).getName().toString());
        assertEquals("BCDE", root.getChildren().get(1).toString());
        
        Data dat = root.getChildren().get(2);
        assertTrue(dat instanceof IntegerData);
        assertEquals(12345, ((IntegerData)dat).getInt());
        
        dat = root.getChildren().get(3);
        assertTrue(dat instanceof IntegerData);
        assertEquals(1234, ((IntegerData)dat).getInt());
        //System.out.println(root.toString());
    }
    
    /**
     * Parse copybook data to a Map.
     *
     * @throws FileNotFoundException
     */
    public void testWeCanParseCopybookDataAsMap() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        Map<String,Object>record = results.get(0).toMap();
        assertEquals(1, record.size());
        assertTrue(record.containsKey("ROOT"));
        @SuppressWarnings("unchecked") Map<String,Object> root = (Map<String,Object>)record.get("ROOT");
        
        assertTrue(root.containsKey("A"));
        assertEquals("ABCDEF", root.get("A"));
        
        assertTrue(root.containsKey("B"));
        assertEquals("BCDE", root.get("B"));
        
        assertTrue(root.containsKey("C"));
        assertEquals(12345, ((BigInteger)root.get("C")).intValue());
        
        assertTrue(root.containsKey("D"));
        assertEquals(1234, ((BigInteger)root.get("D")).intValue());
    }
    
    @SuppressWarnings("unchecked")
    public void testRightTrimOfPICXfields() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        Map<String,Object>record = results.get(0).toMap();
        Map<String,Object> root = (Map<String,Object>)record.get("ROOT");
        
        assertTrue(root.containsKey("SUB"));
        List<Map<String,Object>> sub = (List<Map<String,Object>>)root.get("SUB");
        assertEquals(2, sub.size());
        
        Map<String,Object> subEl = sub.get(0);
        assertTrue(subEl.containsKey("E"));
        assertEquals(" E", subEl.get("E"));
        assertTrue(subEl.containsKey("F"));
        assertEquals("FF", subEl.get("F"));
        
        subEl = sub.get(1);
        assertTrue(subEl.containsKey("E"));
        assertEquals("EEE", subEl.get("E"));
        assertTrue(subEl.containsKey("F"));
        assertEquals("FFF", subEl.get("F"));
    }
    
    @SuppressWarnings("unchecked") 
    public void testOccursAsList() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        Map<String,Object>record = results.get(0).toMap();
        List<Object> sub = (List<Object>) ((Map<String,Object>)record.get("ROOT")).get("SUB");
        assertEquals(2, sub.size());
        assertEquals("EEE", ((Map<String,Object>)sub.get(1)).get("E").toString());
        assertEquals("FFF", ((Map<String,Object>)sub.get(1)).get("F").toString());
        assertEquals("[{E= E, F=FF}, {E=EEE, F=FFF}]", Arrays.toString(sub.toArray()));
        assertEquals("{ROOT={A=ABCDEF, B=BCDE, C=12345, D=1234, SUB=[{E= E, F=FF}, {E=EEE, F=FFF}]}}", record.toString());
    }
    
}
