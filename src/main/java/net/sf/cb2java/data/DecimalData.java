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

public class DecimalData extends NumericData {
	
    private BigDecimal data;
    private int roundingMode = BigDecimal.ROUND_HALF_UP;
    
    public DecimalData(final Numeric definition) {
        super(definition);
    }
    
    public void setRoundingMode(int mode) {
        this.roundingMode = mode;
    }
    
    public float getFloat() {
        return data == null ? 0 : data.floatValue();
    }
    
    public double getDouble() {
        return data == null ? 0 : data.doubleValue();
    }
    
    public BigDecimal getBigDecimal() {
        return data;
    }
    
    @Override
    protected void setValueImpl(Object data) {
        setValue((BigDecimal) data, true);
    }
    
    /**
     * sets the value of this data object with the given floating
     * point number.  Rounds the value to the decimalPlaces specified
     * in the definiton using this object's rounding more.
     * 
     * @param data the value to set this Object to
     */
    public void setValue(double data, boolean validate) {
        BigDecimal value = new BigDecimal(data);
        int decPlaces = ((Numeric) getDefinition()).decimalPlaces();
        value = value.setScale(decPlaces, roundingMode);
        
        setValue(value, validate);
    }
    
    public void setValue(BigDecimal data, boolean validate) {
        if (validate) {
            validate(data);
        }
        this.data = data;
    }
    
    @Override
    public Object getValue() {
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
}