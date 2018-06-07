package net.sf.cb2java.types;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

public class DecimalTest extends TestCase {

	public void testParse() throws UnsupportedEncodingException {
		assertEquals(new BigInteger("9050"), parse("9(4)", "9050"));
		assertEquals(new BigInteger("-9050"), parse("S9(4)", "905}"));
		assertEquals(new BigInteger("-2345"), parse("S9(4)", "234N")); // Real example
		assertEquals(new BigDecimal("2345.67"), parse("9(4)V99", "234567"));
		assertEquals(new BigDecimal("-2345.67"), parse("S9(4)V99", "23456P"));
		assertEquals(new BigDecimal("1.23"), parse("S9(6)V99", "0000012C")); // Real example
	}

	private Object parse(String pic, String string) throws UnsupportedEncodingException {
		Decimal decimal = new Decimal("DUMMY", 0, 1, pic, SignPosition.TRAILING);
		Data data = decimal.parse(string.getBytes("cp1252"));
		Object value = data.getValue();
		return value;
	}
	
	
	public void testIntegerData() {
		Decimal decimal = new Decimal("FILLER", 0, 1, "99", SignPosition.TRAILING);
		Data dat = decimal.create();
		assertTrue(dat instanceof IntegerData);
		assertEquals(BigInteger.ZERO, dat.getValue());
		IntegerData intDat = (IntegerData)dat;
		assertEquals(0, intDat.getInt());
		assertEquals(0, intDat.getLong());
		
		intDat.setValue(1l, true);
		assertEquals(BigInteger.ONE, dat.getValue());
		assertEquals(1, intDat.getInt());
		assertEquals(1, intDat.getLong());
		
		intDat.setValue(BigDecimal.TEN);
		assertEquals(10, intDat.getInt());
	}
	
	public void testDecimalData() {
		Decimal decimal = new Decimal("FILLER", 0, 1, "99V99", SignPosition.TRAILING);
		Data dat = decimal.create();
		assertNull(dat.getValue());
		assertTrue(dat instanceof DecimalData);
		
		DecimalData decDat = (DecimalData)dat;
		assertEquals(0.0f, decDat.getFloat());
		assertEquals(0.0, decDat.getDouble());
		
		dat.setValue(BigDecimal.valueOf(12.34));
		assertEquals(12.34f, decDat.getFloat());
		assertEquals(12.34, decDat.getDouble());
		
		decDat.setRoundingMode(BigDecimal.ROUND_DOWN);
		decDat.setValue(45.678, true);
		assertEquals(45.67, decDat.getDouble());
		
		Exception ex = null;
		try {
			decDat.setValue(BigDecimal.valueOf(-12.34));
			fail();
		} catch (IllegalArgumentException e) {
			ex = e;
		}
		assertNotNull(ex);
	}
}
