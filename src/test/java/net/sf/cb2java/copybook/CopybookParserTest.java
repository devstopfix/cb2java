package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.cb2java.copybook.pojos.B;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.GroupData;
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
        // TODO assertEquals(12345, root.getChildren().get(2).toString());
        // TODO assertEquals(1234, ((Data)root.getChildren().get(3)).getValue());
        System.out.println(root.toString());
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
        assertTrue(record.containsKey("ROOT"));
        //assertEquals("ABCDEF", root.getChildren().get(0).toString());
        //assertEquals("B", ((Data)root.getChildren().get(1)).getName().toString());
        //assertEquals("BCDE", root.getChildren().get(1).toString());
        // TODO assertEquals(12345, root.getChildren().get(2).toString());
        // TODO assertEquals(1234, ((Data)root.getChildren().get(3)).getValue());
    }
    
    public void testRightTrimOfPICXfields() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        Map<String,Object>record = results.get(0).toMap();
        
        // TODO assertEquals(" E", record.get(0).toString());
        // TODO assertEquals("FF", record.get(1).toString());
    }
    
    public void testOccursAsList() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        Map<String,Object>record = results.get(0).toMap();
        List sub = (List) ((Map)record.get("ROOT")).get("SUB");
        assertEquals(2, sub.size());
        assertEquals("EEE", ((Map)sub.get(1)).get("E").toString());
        assertEquals("FFF", ((Map)sub.get(1)).get("F").toString());
        System.out.println(record.toString());
        System.out.println(Arrays.toString(sub.toArray()));
    }
    
    public void testGetPath() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        
        Record root = results.get(0);
        
        assertEquals("E", root.getChildFromPath("ROOT.SUB[1].E").toString());
        assertEquals("FF", root.getChildFromPath("ROOT.SUB[1].F").toString());
        
        assertEquals("EEE", root.getChildFromPath("ROOT.SUB[2].E").toString());
        assertEquals("FFF", root.getChildFromPath("ROOT.SUB[2].F").toString());

        System.out.println(String.format("ROOT.SUB[1].E = '%s'", root.getChildFromPath("ROOT.SUB[1].E").toString()));
        System.out.println(String.format("ROOT.SUB[1].F = '%s'", root.getChildFromPath("ROOT.SUB[1].F").toString()));
        System.out.println(String.format("ROOT.SUB[2].E = '%s'", root.getChildFromPath("ROOT.SUB[2].E").toString()));
        System.out.println(String.format("ROOT.SUB[2].F = '%s'", root.getChildFromPath("ROOT.SUB[2].F").toString()));

        System.out.println(root.toString());
        //System.out.println(Arrays.toString(sub.toArray()));
    }
    
    
    public void testMapPojoAnnotation(){
    	HashMap<String, Field> ret = Copybook.mapPojo(B.class);
    	
    }
    public void testToPojoObject() throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        
        Record root = results.get(0);
        
        B pojo = root.toPOJO(B.class);
        
        assertEquals("E",  pojo.getSubs().get(0).getE());
        assertEquals("FF", pojo.getSubs().get(0).getF());
        
        assertEquals("EEE", pojo.getSubs().get(1).getE());
        assertEquals("FFF", pojo.getSubs().get(1).getF());

        System.out.println(String.format("ROOT.SUB[1].E = '%s'", pojo.getSubs().get(0).getE()));
        System.out.println(String.format("ROOT.SUB[1].F = '%s'", pojo.getSubs().get(0).getF()));
        System.out.println(String.format("ROOT.SUB[2].E = '%s'", pojo.getSubs().get(1).getE()));
        System.out.println(String.format("ROOT.SUB[2].F = '%s'", pojo.getSubs().get(1).getF()));

        System.out.println(root.toString());
        //System.out.println(Arrays.toString(sub.toArray()));
    }
}
