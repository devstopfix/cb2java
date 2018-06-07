package net.sf.cb2java.types;

import junit.framework.TestCase;

public class NumericTest extends TestCase  {

	public void testLength() {
		assertEquals(1, Numeric.getLength("9"));
		assertEquals(1, Numeric.getLength("9(1)"));
		assertEquals(3, Numeric.getLength("999"));
		assertEquals(3, Numeric.getLength("9(3)"));
	
		assertEquals(3, Numeric.getLength("9V99"));
		assertEquals(3, Numeric.getLength("9V9(2)"));
	}
	
	public void testScale() {
		assertEquals(0, Numeric.getScale("9"));
		assertEquals(0, Numeric.getScale("9(1)"));
		assertEquals(0, Numeric.getScale("999"));
		assertEquals(0, Numeric.getScale("9(3)"));
		
		assertEquals(2, Numeric.getScale("9V99"));
		assertEquals(2, Numeric.getScale("9V9(2)"));
	}
}
