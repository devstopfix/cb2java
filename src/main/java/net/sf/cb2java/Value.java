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
import java.util.Arrays;

public abstract class Value
{
    public static final byte[] EMPTY_BYTES = {};
    
    public static final Side LEFT = new Side();
    public static final Side RIGHT = new Side();
    
    private final Values values;
    
    public Value(Values values)
    {
        this.values = values;
    }
    
    public String getEncoding()
    {
        return values.getEncoding();
    }
    
    public byte[] get(int length)
    {
        return fill(length);
    }
    
    public abstract byte getByte();
    
    public String fillString(int length)
    {
        return fillString("", length, LEFT);
    }
        
    public String fillString(String s, int length, Side side)
    {
        try {
            return new String(fill(s.getBytes(getEncoding()), length, side), getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
        
    public byte[] fill(int length)
    {
        return fill(EMPTY_BYTES, length, LEFT);
    }
    
    public byte[] fill(byte[] bytes, int length, Side side)
    {
        if (length <= 0) {
            return EMPTY_BYTES;
        }
        
        byte[] out = new byte[length];
        Arrays.fill(out, getByte());
        
        if (side == LEFT) {
            System.arraycopy(bytes, 0, out, length - bytes.length, bytes.length);
        } else {
            System.arraycopy(bytes, 0, out, 0, bytes.length);
        }
        
        return out;
    }
    
    public static final class Side
    {
        private Side() {}
    }
}