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

import net.sf.cb2java.Value;
import net.sf.cb2java.data.CharData;
import net.sf.cb2java.data.Data;

/** 
 * Class used to represent alpha and alphanumeric data types.
 * 
 * @author James Watson
 */
public class Characters extends Leaf {
    private final int length;
    
    public Characters(String name, int length, int level, int occurs) {
        super(name, level, occurs);
        this.length = length;
    }
    
    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Data create() {
        return new CharData(this);
    }
    
    @Override
    public Data parse(byte[] bytes) {
        CharData data = (CharData) create();
        data.setValue(getString(bytes));
        return data;
    }
    
    @Override
    public void validate(Object data) {
        if (data == null) return;

        String s = data.toString();
        
        if (s.length() > getLength()) {
        	throw new IllegalArgumentException("string value of " + data + " is longer than " + length);
        }
    }

    @Override
    public byte[] toBytes(Object data) {
        byte[] output = data == null ? new byte[0] : getBytes((String) data);
        
        return getValue().fill(output, getLength(), Value.RIGHT);
    }

    @Override
    public Value getValue() {
        Value result = super.getValue();
        return result == null ? getSettings().getValues().SPACES : result;
    }
}