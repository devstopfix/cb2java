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
import net.sf.cb2java.types.Numeric;

public class DecimalData extends NumericData
{
    private int roundingMode = BigDecimal.ROUND_HALF_UP;
    private BigDecimal data;
    
    public DecimalData(final Numeric definition)
    {
        super(definition);
    }
    
    public void setRoundingMode(int mode)
    {
        this.roundingMode = mode;
    }
    
    public float getFloat()
    {
        return data == null ? 0 : data.floatValue();
    }
    
    public double getDouble()
    {
        return data == null ? 0 : data.doubleValue();
    }
    
    public BigDecimal getBigDecimal()
    {
        return data;// == null ? new BigDecimal(0) : data;
    }
    
    /**
     * sets the value of this data object with the given floating
     * point number.  Rounds the value to the decimalPlaces specified
     * in the definiton using this object's rounding more.
     * 
     * @param data the value to set this Object to
     */
    public void setValue(double data)
    {
        BigDecimal temp = new BigDecimal(data);
        temp = temp.setScale(((Numeric) getDefinition()).decimalPlaces(), roundingMode);
        
        setValue(temp, true);
    }
    
    protected void setValueImpl(Object data)
    {   
        setValue((BigDecimal) data, false);
    }
    
    public void setValue(BigDecimal data)
    {   
        setValue(data, true);
    }
    
    public void setValue(BigDecimal data, boolean validate)
    {   
        if (validate) validate(data);
        this.data = data;
    }
    
    public Object getValue()
    {
        return getBigDecimal();
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
        return this.getBigDecimal();
    }

	@Override
	public <T> T toPOJO(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		if(clazz.equals(Integer.class)){
			return (T) Integer.valueOf(((BigDecimal)getValue()).intValue());
		}else if(clazz.equals(Long.class)){
			return (T) Long.valueOf(((BigDecimal)getValue()).longValue());
		}else if(clazz.equals(Double.class)){
			return (T) Double.valueOf(((BigDecimal)getValue()).doubleValue());
		}else if(clazz.equals(Boolean.class)){
			return (T) Boolean.valueOf(((BigDecimal)getValue()).byteValue()>0);
		} 
		
		return (T) ((BigDecimal)getValue());
	}
}