package net.sf.cb2java;

import java.nio.charset.UnsupportedCharsetException;

import junit.framework.TestCase;

public class ValuesTest extends TestCase {

	public void testUnknownEncoding() {
		Exception ex = null;
		try {
			Values.testEncoding("FooBar");
			fail();
		} catch (UnsupportedCharsetException e) {
			ex = e;
		}
		assertEquals("FooBar", ex.getMessage());
	}
	
	public void testEncodingUTF8() {
		Exception ex = null;
		try {
			Values.testEncoding("UTF-8");
			fail();
		} catch (UnsupportedCharsetException e) {
			ex = e;
		}
		assertEquals("UTF-8 is not a single-byte encoding", ex.getMessage());
	}
	
	public void testEncodingUTF16() {
		Exception ex = null;
		try {
			Values.testEncoding("UTF-16");
			fail();
		} catch (UnsupportedCharsetException e) {
			ex = e;
		}
		assertEquals("UTF-16 is not a single-byte encoding", ex.getMessage());
	}
	
	public void testEncodingIBM420() {
		Exception ex = null;
		try {
			Values.testEncoding("IBM420");
			fail();
		} catch (UnsupportedCharsetException e) {
			ex = e;
		}
		assertTrue(ex.getMessage().startsWith("IBM420 recoded '!/09?@ AZ[]`az/!|' as '!/09?@ AZ"));
	}
}
