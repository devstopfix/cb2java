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
package net.sf.cb2java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.sf.cb2java.types.SignPosition;

public class Settings {

	String encoding = System.getProperty("file.encoding");
	boolean littleEndian = false;
	String floatConversion = "net.sf.cb2java.copybook.floating.IEEE754";
	SignPosition signPosition = SignPosition.TRAILING;
	int columnStart = 6;
	int columnEnd = 72;
	Values values = new Values();

	static Settings DEFAULT;
	static public Settings DEFAULT() {
		if(DEFAULT == null) {
			DEFAULT = new Settings();
			Properties props = new Properties();

			try (InputStream is = Settings.class.getResourceAsStream("/copybook.props")) {
				if (is == null) {
					System.out.println("Could not load 'copybook.props' file, reverting to defaults.");
				} else {
					props.load(is);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not load 'copybook.props' file, reverting to defaults.");
			}

			DEFAULT.setEncoding(getSetting("encoding", DEFAULT.getEncoding(), props));
			DEFAULT.setLittleEndian("false".equals(getSetting("little-endian", DEFAULT.isLittleEndian() + "", props)));
			DEFAULT.setFloatConversion(getSetting("float-conversion", DEFAULT.getFloatConversion(), props));
			DEFAULT.setSignPosition("leading".equalsIgnoreCase(getSetting("default-sign-position", "trailing", props))
					? SignPosition.LEADING : SignPosition.TRAILING);
			DEFAULT.setColumnStart(Integer.parseInt(getSetting("column.start", DEFAULT.getColumnStart() + "", props)));
			DEFAULT.setColumnEnd(Integer.parseInt(getSetting("column.end", DEFAULT.getColumnEnd() + "", props)));
		}
		return DEFAULT;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isLittleEndian() {
		return littleEndian;
	}

	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
	}

	public String getFloatConversion() {
		return floatConversion;
	}

	public void setFloatConversion(String floatConversion) {
		this.floatConversion = floatConversion;
	}

	public SignPosition getSignPosition() {
		return signPosition;
	}

	public void setSignPosition(SignPosition signPosition) {
		this.signPosition = signPosition;
	}

	public int getColumnStart() {
		return columnStart;
	}

	public void setColumnStart(int columnStart) {
		this.columnStart = columnStart;
	}

	public int getColumnEnd() {
		return columnEnd;
	}

	public void setColumnEnd(int columnEnd) {
		this.columnEnd = columnEnd;
	}

	public Values getValues() {
		return values;
	}

	public void setValues(Values values) {
		this.values = values;
	}

	static private String getSetting(String name, String defaultValue, Properties props) {
		String result = defaultValue;
		try {
			result = System.getProperty("cb2java." + name, result);
			result = props.getProperty(name, result);
		} catch (RuntimeException e) {
		}
		return result;
	}
}
