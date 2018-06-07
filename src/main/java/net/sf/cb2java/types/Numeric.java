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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

/**
 * Base type for numeric elements.
 *
 * @author Matt Watson
 */
public abstract class Numeric extends Leaf {
	
    private final int length;
    private final int decimalPlaces;
    private final boolean signed;
    private final String picture;
    
    protected Numeric(String name, int level, int occurs, final String picture) {
        super(name, level, occurs);
        this.picture = picture;
        this.length = getLength(picture);
        this.decimalPlaces = getScale(picture);
        this.signed = isSigned(picture);
    }
    
    private static boolean isSigned(String picture) {
        return picture.charAt(0) == 'S';
    }
    
    protected static int getLength(String pic) {
        int length = 0;
        
        for (int i = 0; i < pic.length(); i++) {
            char c = pic.charAt(i);
            /**
             * I don't remember why i added 'S' to length calculation...
             * but this breaks tests :(
             */
            if (/*c == 'S' ||*/ c == '9' && (i == pic.length() - 1 || pic.charAt(i + 1) != '(')) {
                length++;
            } else if (c == '(') {
                int pos = pic.indexOf(')', i);
                int times = Integer.parseInt(pic.substring(i + 1, pos));
                i = pos;
                length += times;
            }
        }
        
        return length;
    }
    
    protected static int getScale(String pic) {
    	int vPos = pic.indexOf("V");
    	if (vPos < 0) {
    		return 0;
    	} else {
    		return getLength(pic.substring(vPos));
    	}
    }
    
    protected final boolean signed() {
        return signed;
    }
    
    public abstract int digits();
    
    @Override
    public int getLength() {
        return length;
    }
    
    public int decimalPlaces() {
        return decimalPlaces;
    }
    
    public DecimalFormat getFormatObject() {
        StringBuffer buffer = new StringBuffer("#");
                
        for (int i = 0; i < digits(); i++) {
            if (i + decimalPlaces() == digits()) {
                buffer.append('.');
            }
            buffer.append('0');
        }
        
        if (decimalPlaces() < 1) {
        	buffer.append('.');
        }
        
        buffer.append('#');
                
        return new DecimalFormat(buffer.toString());
    }
    
    /**
     * validates the data with the given decimal (printed) length
     * constraint.  This is useful for types where the length in
     * the application data is not the same as the logical length
     * e.g. SignedSeparate.
     * 
     * @param data
     */
    @Override
    public void validate(Object data) {
        if (data == null) {
        	return;
        }
        
        BigDecimal bigD = (data instanceof BigInteger) ? new BigDecimal((BigInteger) data) : (BigDecimal) data;
        boolean negative = BigDecimal.ZERO.compareTo(bigD) > 0;
        
        if (negative && !signed()) {
            throw (IllegalArgumentException) createEx(bigD, getName() 
                + " is not signed").fillInStackTrace();
        }
        
        int scale = bigD.scale();
        
        if (decimalPlaces() > 0 && scale > decimalPlaces()) {
            throw (IllegalArgumentException) createEx(bigD, "must have " 
                + decimalPlaces() + " decimal places").fillInStackTrace();
        }
        
        bigD = bigD.setScale(decimalPlaces());
        String s = bigD.unscaledValue().toString();
        
        if (negative) {
        	s = s.substring(1);
        }
            
        if (s.length() > digits()) {
            throw (IllegalArgumentException) createEx(bigD, "must be no longer than " 
                + digits() + " digits").fillInStackTrace();
        }
    }
    
    @Override
    public Value getValue() {
        Value result = super.getValue();
        return result == null ? getSettings().getValues().ZEROES : result;
    }
    
    protected BigInteger getUnscaled(Object data) {
        if (data instanceof BigInteger) {
            return (BigInteger) data;
        } else {
            int places = decimalPlaces();
            BigDecimal bigD = (BigDecimal) data;
            if (bigD.scale() != places) {
                bigD = bigD.setScale(decimalPlaces());
            }
            
            return bigD.unscaledValue();
        }
    }
    
    @Override
    public Data create() {
    	return (decimalPlaces() > 0) ? new DecimalData(this) : new IntegerData(this);
    }
    
    private IllegalArgumentException createEx(BigDecimal data, String reason) {
        return createEx(data, reason, null);
    }
    
    private IllegalArgumentException createEx(BigDecimal data, String reason, Throwable cause) {
        return new IllegalArgumentException(data + " is not valid for " + getName() + ". " + reason 
            + (cause == null ? "" : " " + cause.getMessage()));
    }
    
	public String getPicture() {
		return picture;
	}

}