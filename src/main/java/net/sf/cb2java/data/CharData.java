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
package net.sf.cb2java.data;

import net.sf.cb2java.types.Characters;

/**
 * Represents data for alpha-numeric data types.
 * 
 * @author James Watson
 */
public class CharData extends ValueData {

    private String data;
    
    public CharData(final Characters definition) {
        super(definition);
    }
    
    /**
     * @return the trimmed String
     */
    public String getString() {
        return data == null ? "" : trimRight(data);
    }
    
    @Override
    public Object getValue() {
        return getString();
    }
    
    @Override
    protected void setValueImpl(Object data) {
        setValue((String) data, true);
    }
    
    /**
     * sets the data as a String
     * @param data
     */
    public void setValue(String data, boolean validate) {
        if (validate) {
            validate(data);
        }
        this.data = data;
    }
    
    public String toString() {
        return getString();
    }

    @Override
    public Object translate(String data) {
        return data;
    }

    /**
     * Convert the copybook data types into standard Java structures
     * and objects.
     * 
     * @author github.com/devstopfix/cb2java
     * @return the copybook data as Plain Java Objects
     */
    @Override
    protected Object toPOJO() {
        return this.getString();
    }

    private static String trimRight(String s) {
        int i = s.length()-1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0,i+1);
    }
}