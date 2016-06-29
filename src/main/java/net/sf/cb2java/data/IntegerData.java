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
public class IntegerData extends NumericData
{
    public BigInteger data;
    
    public IntegerData(Numeric definition)
    {
        super(definition);
    }
    
    public int getInt()
    {
        return data == null ? 0 : data.intValue();
    }
    
    public long getLong()
    {
        return data == null ? 0 : data.longValue();
    }
    
    public BigInteger getBigInteger()
    {
        return data == null ? new BigInteger("0") : data;
    }
    
    public void setValue(long data)
    {
        BigInteger temp = BigInteger.valueOf(data);
        setValue(temp, true);
    }
    
    protected void setValueImpl(Object data)
    {
        setValue(((BigDecimal) data).toBigInteger(), false);
    }
    
    public void setValue(BigInteger data)
    {
        setValue(data, true);
    }

    public void setValue(BigInteger data, boolean validate)
    {
        if (validate) validate(data);
        this.data = data;
    }
    
    public Object getValue()
    {
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

	@Override
	public <T> T toPOJO(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		if(clazz.equals(Integer.class)){
			return (T) Integer.valueOf(((Integer)getValue()).intValue());
		}else if(clazz.equals(Long.class)){
			return (T) Long.valueOf(((Integer)getValue()).longValue());
		}else if(clazz.equals(Double.class)){
			return (T) Double.valueOf(((Integer)getValue()).doubleValue());
		}else if(clazz.equals(Boolean.class)){
			return (T) Boolean.valueOf(((Integer)getValue()).byteValue()>0);
		} 
		
		return (T) ((Integer)getValue());
	}
}