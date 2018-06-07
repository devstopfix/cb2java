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
package net.sf.cb2java.copybook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import net.sf.cb2java.Settings;

/**
 * Very simple COBOL pre-processor that chops the left and right margins. Column
 * start and end positions are configurable using a properties file. Linefeeds
 * are retained as these are required by the main parser. COBOL files typically
 * contain some junk characters and comment indicators in the "margins" and this
 * routine removes those.
 * 
 * @author Peter Thomas
 */
public class CobolPreprocessor {

	private CobolPreprocessor() {
	}

	public static String preProcess(Reader reader, Settings settings) {
    	// TODO: figure out a way to pass copybook specific settings for non-default margins treated as comment.
		int columnStart = settings.getColumnStart();
		int columnEnd = settings.getColumnEnd();

		StringBuffer sb = new StringBuffer();

		try (BufferedReader buffer = new BufferedReader(reader)) {
			String s = null;
			while ((s = buffer.readLine()) != null) {
				if (s.length() > columnStart) {
					int thisColumnStart = columnStart;
					if (s.charAt(columnStart) == '/') {
						sb.append('*');
						thisColumnStart++;
					}
					if (s.length() < columnEnd) {
						sb.append(s.substring(thisColumnStart));
					} else {
						sb.append(s.substring(thisColumnStart, columnEnd));
					}
				}
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return sb.toString();
	}
}