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

import java.math.BigDecimal;
import java.math.BigInteger;
import net.sf.cb2java.types.Numeric;

/**
 * class that represents numeric data 
 * with no fraction portion
 * 
 * @author James Watson
 */
public class IntegerData extends NumericData {
	
    private BigInteger data;
    
    public IntegerData(Numeric definition) {
        super(definition);
    }
    
    public int getInt() {
        return data == null ? 0 : data.intValue();
    }
    
    public long getLong() {
        return data == null ? 0 : data.longValue();
    }
    
    public BigInteger getBigInteger() {
        return data == null ? BigInteger.ZERO : data;
    }
    
    @Override
    protected void setValueImpl(Object data) {
        setValue(((BigDecimal) data).toBigInteger(), true);
    }
    
    public void setValue(BigInteger data) {
    	setValue(data, true);
    }
    
    public void setValue(long data, boolean validate) {
        BigInteger temp = BigInteger.valueOf(data);
        setValue(temp, validate);
    }
    
    public void setValue(BigInteger data, boolean validate) {
        if (validate) {
            validate(data);
        }
        this.data = data;
    }
    
    @Override
    public Object getValue() {
        return getBigInteger();
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
        return this.getBigInteger();
    }
}