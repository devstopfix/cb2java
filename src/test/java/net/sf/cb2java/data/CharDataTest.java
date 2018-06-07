package net.sf.cb2java.data;

import junit.framework.TestCase;
import net.sf.cb2java.types.AlphaNumeric;

public class CharDataTest extends TestCase {

	public void testCharData() {
		AlphaNumeric field = new AlphaNumeric("FILLER", 1, 1, "X(10)");
		CharData data = (CharData)field.create();
		
		// uninitialised value should be empty string
		assertEquals("", data.getValue());
		
		// setting an empty string should get truncated
		data.setValue("  ");
		assertEquals("", data.getValue());
		
		// leading spaces should not get truncated
		data.setValue("  foo bar");
		assertEquals("  foo bar", data.getValue());
		
		// trailing spaces should be truncated
		data.setValue("foo bar  ");
		assertEquals("foo bar", data.getValue());
		
		// length overflow should be noticed only on validation
		data.setValue("foo bar code station", false);
		Exception ex = null;
		try {
			data.setValue("foo bar code station");
			fail();
		} catch (IllegalArgumentException e) {
			ex = e;
		}
		assertEquals("'foo bar code station' does not match pattern 'X(10)' specified for FILLER", ex.getMessage());
	}

}
