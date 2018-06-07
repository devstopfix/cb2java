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
import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

public class SignedSeparate extends SignedNumeric {
    
    public SignedSeparate(String name, int level, int occurs, String picture, SignPosition signPosition) {
        super(name, level, occurs, picture, signPosition);
    }
    
    @Override
    public Data parse(byte[] bytes) {
        String s = getString(bytes);
        
        char sign;
        
        if (getSignPosition() == SignPosition.LEADING) {
            sign = s.charAt(0);
        } else if (getSignPosition() == SignPosition.TRAILING) {
            sign = s.charAt(s.length() - 1);
            s = sign + s.substring(0, s.length() - 1);
        } else {
            throw new IllegalStateException("undefined sign position");
        }
        
        if (sign != '+' && sign != '-') {
        	throw new IllegalArgumentException(getName() + " is sign separate "
            + (getSignPosition() == SignPosition.LEADING ? "leading" : "trailing") + " but no sign was found on value " + s);
        }
        
        if (sign == '+') {
        	s = s.substring(1);
        }

        BigInteger big = new BigInteger(s);
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            dData.setValue(new BigDecimal(big, decimalPlaces()));
        } else {
            IntegerData iData = (IntegerData) data;
            iData.setValue(big);
        }
        return data;
    }
    
    @Override
    public byte[] toBytes(Object data) {
        String s;
        boolean positive;
        
        if (data == null) {
            positive = true;
            s = "";
        } else {
            BigInteger bigI = getUnscaled(data);
            positive = BigDecimal.ZERO.unscaledValue().compareTo(bigI) < 0;
            s = bigI.toString();
        }
        
        char sign = positive ? '+' : '-';
        byte[] temp = getValue().fill(getBytes(s), getLength() - 1, Value.LEFT);
        byte[] output = new byte[getLength()];
        
        if (getSignPosition() == SignPosition.TRAILING) {
            System.arraycopy(temp, 0, output, 0, temp.length);
            output[output.length - 1] = (byte) sign;
        } else if (getSignPosition() == SignPosition.LEADING) {
            System.arraycopy(temp, 0, output, 1, temp.length);
            output[0] = (byte) sign;
        } else {
            throw new IllegalStateException("undefined sign position");
        }
        
        return output;
    }

    @Override
    public int digits() {
        return getLength() - 1;
    }
}