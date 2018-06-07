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
package net.sf.cb2java;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class Values {
	
    private String encoding;
    
    public Values() {
        ((StringBasedValue) SPACES).bite = ' ';
        ((StringBasedValue) QUOTES).bite = '"';
        ((StringBasedValue) ZEROES).bite = '0';
    }
    
    public void setEncoding(String encoding) {
        testEncoding(encoding);
        this.encoding = encoding;
        
        try {
            ((StringBasedValue) SPACES).bite = " ".getBytes(encoding)[0];
            ((StringBasedValue) QUOTES).bite = "\"".getBytes(encoding)[0];
            ((StringBasedValue) ZEROES).bite = "0".getBytes(encoding)[0];
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedCharsetException(encoding);
        }
    }
    
    static void testEncoding(String encoding) {
    	String testString = "!/09?@ AZ[]`az/!|";
    	Charset charset = Charset.forName(encoding);
		ByteBuffer encoded = charset.encode(testString);
		if (encoded.array().length != testString.length()) {
			throw new UnsupportedCharsetException(encoding + " is not a single-byte encoding");
		}
		String decoded = charset.decode(encoded).toString();
		if (!decoded.equals(testString)) {
			throw new UnsupportedCharsetException(encoding + " recoded '" + testString + "' as '" + decoded + "'");
		}
    }
    
    protected String getEncoding() {
		return encoding == null ? "cp1252" : encoding;
	}
    
    public class Literal extends Value
    {
        private final String value;
        
        public Literal(final String value)
        {
            super(Values.this);
            this.value = value;
        }

        @Override
        public byte[] get(int length)
        {
            String s = value.length() > length ? value.substring(0, length) : value;
            
            try {
                return s.getBytes(this.getEncoding());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public byte getByte()
        {
            return 0;
        }
    }
    
    private class StringBasedValue extends Value
    {
        public StringBasedValue(Values parent)
        {
            super(Values.this);
        }

        byte bite;
        
        public byte getByte()
        {
            return bite;
        }
    }
    
    public final Value SPACES = new StringBasedValue(Values.this);
    
    public final Value LOW_VALUES = new Value(Values.this) {

        public byte getByte()
        {
            return 0;
        }
    };
    
    public final Value HIGH_VALUES = new Value(Values.this) {

        public byte getByte()
        {
            return -1;
        }
    };
    
    public final Value ZEROES = new StringBasedValue(Values.this);
    
    public final Value QUOTES = new StringBasedValue(Values.this);
    
    public final Value NULLS = LOW_VALUES;
}