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
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

public class Packed extends SignedNumeric {
	
    private final int digits;
    private final int length;

    public Packed(String name, int level, int occurs, String picture, SignPosition signPosition) {
        super(name, level, occurs, picture, signPosition);
        digits = super.getLength();
        length = getLength(digits);
    }

    protected static final int getLength(int digits) {
        return (digits / 2) + 1;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int digits() {
        return digits;
    }

    @Override
    public Data parse(byte[] input) {
        byte lastByte = input[input.length - 1];
        boolean negative = signed() && (lastByte & 0x0F) == 0x0D;
        BigInteger bigI = BigInteger.ZERO;
        int numberLength = (length * 2) - 1;

        for (int i = 0; i < numberLength; i++) {
            byte current = input[i / 2];
            // if the index is even, use the left nibble odd, use the right nibble
            if (i % 2 == 0) {
                current = (byte) ((current & 0xF0) >>> 4);
            } else {
                current = (byte) (current & 0x0F);
            }

            /*
             * magnitude is the one more than the reverse of the index so if
             * there are 9 digits, the magnitude at i = 0, is 8, the magnitude
             * at i = 8 is 0
             */
            int magnitude = numberLength - (i + 1);
            BigInteger temp = BigInteger.TEN.pow(magnitude);
            bigI = bigI.add(temp.multiply(BigInteger.valueOf(current)));
        }

        if (negative) {
            bigI = bigI.negate();
        }

        Data data = create();

        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            BigDecimal bigD = new BigDecimal(bigI, decimalPlaces());
            dData.setValue(bigD);
        } else {
            IntegerData iData = (IntegerData) data;
            iData.setValue(bigI);
        }
        return data;
    }

    @Override
    public byte[] toBytes(Object data) {
        BigInteger bigI = (data == null) ? BigInteger.ZERO : getUnscaled(data);
        byte signNibble = signed() ? (byte) (bigI.compareTo(BigInteger.ZERO) < 0 ? 0x0D : 0x0C) : 0x0F;
        byte[] bytes = new byte[getLength()];
        int numberLength = (length * 2) - 1;

        for (int i = numberLength; i > 0; i--) {
            int value = bigI.mod(BigInteger.TEN).intValue();
            int index = (i - 1) / 2;

            if (i % 2 == 0) {
                bytes[index] = (byte) (bytes[index] | value);
            } else {
                bytes[index] = (byte) (bytes[index] | (value << 4));
            }

            bigI = bigI.divide(BigInteger.TEN);
        }

        int signByte = bytes.length - 1;
        bytes[signByte] = (byte) (bytes[signByte] | signNibble);
        return bytes;
    }
}