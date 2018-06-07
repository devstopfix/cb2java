package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A map of named Copybook definitions.
 *
 * @author 
 */
public class Copybooks {
    
    public static Map<String, Copybook> readCopybooks(List<File> files) throws FileNotFoundException {
        return readCopybooks(files.toArray(new File[] {}));
    }

    /*
     * Make a map of copybook name (the lowercase filename without extension)
     * to Copybook instance for each given file.
     */
    public static Map<String, Copybook> readCopybooks(File[] files) throws FileNotFoundException {
        Map<String, Copybook> copybooks = new TreeMap<String, Copybook>();
        for(File f:files) {
             String copybookName = copybookNameOfFile(f);
             try (FileInputStream fin = new FileInputStream(f)) {
                copybooks.put(copybookName, CopybookParser.parse(copybookName, fin));
             } catch (IOException | RuntimeException e) {
                 throw new RuntimeException(String.format("Cannot parse copybook structure in file '%s'", f.getName()), e);
             }
        }
        return copybooks;
    }
    
    public static String copybookNameOfFile(File f) {
        if (f.getName().contains("."))
            return f.getName().toLowerCase().split("\\.")[0];
        else
            return f.getName().toLowerCase();
    }
    
}