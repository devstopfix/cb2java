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
import net.sf.cb2java.types.Floating;

/**
 * Floating point representations are hardware specific
 * so the precision that is supported by the underlying type
 * may vary from platform to platform.  For most normal uses
 * Java floats or doubles should be safe to use but it's possible 
 * that there may be a loss of precision if the underlying platform
 * does not follow the IEEE 754 spec.
 * 
 * <p>Because not all floating types are the same in COBOL
 * BigDecimal is the 'natural' type for this class, however, numbers
 * with fraction portions that are not representable in binary will be
 * rounded on setting the data.
 * 
 * @author James Watson
 */
public class FloatingData extends ValueData {
	
    /** All floats should be representable as BigDecimals */
    private BigDecimal data;
    
    /**
     * constructor
     * 
     * @param definition the underlying definition for the
     * type in the copybook definition
     */
    public FloatingData(final Floating definition) {
        super(definition);
    }

    /**
     * gets the big decimal representation of the value
     */
    public BigDecimal getBigDecimal() {
        return data == null ? BigDecimal.ZERO : data;
    }
    
    @Override
    public void setValueImpl(Object data) {
        setValue((BigDecimal) data, true);
    }

    public void setValue(BigDecimal data, boolean validate) { 
        if (validate) {
            validate(data);
        }
        this.data = data;
    }
    
    @Override
    public String toString() {
        return getValue().toString();
    }

    /**
     * returns the internal data as a BigDecimal
     */
    @Override
    public Object getValue() {
        return getBigDecimal();
    }
    
    /**
     * returns the internal data as a BigDecimal
     */
    public float getFloat() {
        return getBigDecimal().floatValue();
    }
    
    /**
     * returns the internal data as a BigDecimal
     */
    public double getDouble() {
        return getBigDecimal().doubleValue();
    }

    @Override
    public Object translate(String data) {
        return new BigDecimal(data);
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