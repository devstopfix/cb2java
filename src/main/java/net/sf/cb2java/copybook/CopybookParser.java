/**
 *    cb2java - Dynamic COBOL copybook parser for Java.
 *    Copyright (C) 2006 James Watson
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 1, or (at your option)
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.cb2java.copybook;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import net.sf.cb2java.Settings;
import net.sf.cb2xml.sablecc.lexer.Lexer;
import net.sf.cb2xml.sablecc.lexer.LexerException;
import net.sf.cb2xml.sablecc.node.Start;
import net.sf.cb2xml.sablecc.parser.Parser;
import net.sf.cb2xml.sablecc.parser.ParserException;

/**
 * 
 * This class is the starting point for parsing copybooks
 * 
 * <p>To parse or create data, you need to first parse the
 * copybook.  The returned CopyBook instance will allow for 
 * working with data.
 * 
 * @author James Watson
 */
public class CopybookParser
{
    /** turns debugging on/off I want to replace this with a logging solution */
    private static boolean debug = false;
    
    /**
     * Parses a copybook definition and returns a Copybook instance
     * 
     * @param name the name of the copybook.  For future use.
     * @param stream the copybook definition's source stream
     * 
     * @return a copybook instance containing the parse tree for the definition
     */
    public static Copybook parse(String name, InputStream stream)
    {        
        return parse(name, new InputStreamReader(stream));
    }

    public static Copybook parse(Settings settings, String name, InputStream stream)
    {
        return parse(settings, name, new InputStreamReader(stream));
    }

    /**
     * Parses a copybook definition and returns a Copybook instance
     * 
     * @param name the name of the copybook.  For future use.
     * @param reader the copybook definition's source reader
     * 
     * @return a copybook instance containing the parse tree for the definition
     */
    public static Copybook parse(Settings settings, String name, Reader reader)
    {        
        String preProcessed = CobolPreprocessor.preProcess(reader, settings);
        StringReader sr = new StringReader(preProcessed);
        PushbackReader pbr = new PushbackReader(sr, 1000);
        
        Lexer lexer = debug ? new DebugLexer(pbr) : new Lexer(pbr);
        
        Parser parser = new Parser(lexer);
        CopybookAnalyzer copyBookAnalyzer = new CopybookAnalyzer(name, parser, settings);
        Start ast;
        try {
			ast = parser.parse();
        } catch (ParserException | LexerException | IOException e) {
            throw new RuntimeException("fatal parse error\n"
                + (lexer instanceof DebugLexer 
                ? "=== buffer dump start ===\n"
                + ((DebugLexer) lexer).getBuffer()
                + "\n=== buffer dump end ===" : ""), e);
        }
        ast.apply(copyBookAnalyzer);
        
        return copyBookAnalyzer.getDocument();
    }

    public static Copybook parse(String name, Reader reader) {

        return parse(Settings.DEFAULT(), name, reader);
    }

}