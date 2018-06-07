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
package net.sf.cb2java.types;

import java.util.regex.Pattern;
import net.sf.cb2java.Value;

/** 
 * Class used to represent alpha and alphanumeric data types.
 * 
 * @author James Watson
 */
public class AlphaNumeric extends Characters {
	
    /** the original pattern used for debugging */
    private final String originalPattern;
    /** the regex pattern for validation */
    private final Pattern pattern;
    /** the byte length of this element */
    private final int length;
    
    public AlphaNumeric(String name, int level, int occurs, String pattern) {
        super(name, 0, level, occurs);
        
        this.originalPattern = pattern;
        pattern = pattern.toUpperCase();
        StringBuffer buffer = new StringBuffer();
        length = parsePattern(pattern, buffer);
        this.pattern = Pattern.compile(buffer.toString());
    }
    
    @Override
    public int getLength() {
        return length;
    }
    
    private static int parsePattern(String pattern, StringBuffer buffer) {
        boolean open = false;
        int length = 0;
        
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            
            if (c == '(') {
                int pos = pattern.indexOf(')', i);
                int times = Integer.parseInt(pattern.substring(i + 1, pos));
                buffer.append('{').append(times).append('}');
                i = pos + 1;
                length += times;
                open = false;
            } else {
                if (open) length++;
                buffer.append(forChar(c));
                open = true;
            }
        }
        
        if (open) {
        	length++;
        }
        
        return length;
    }
    
    private static String forChar(char c) {
        switch (c) {
        case 'A':
            return "[a-zA-Z\u0000 ]";
        case 'X':
            return ".";//[a-zA-Z0-9\u0000 ]
        case '9':
            return "[0-9\u0000 ]";
        default:
            throw new IllegalArgumentException("character [" + c + "] not allowed.");
        }
    }
    
    @Override
    public void validate(Object data) {
        if (data == null) {
        	return;
        }
        
        String value = (String)data;
        int len = Math.max(value.length(), this.getLength());
        String filledValue = this.getValue().fillString(value, len, Value.RIGHT);
        
        if (!pattern.matcher(filledValue).matches()) {
            throw new IllegalArgumentException("'" + value + "' does not match pattern '" + originalPattern
                + "' specified for " + getName());
        }
    }
}