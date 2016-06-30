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

import net.sf.cb2java.types.Characters;

/**
 * Represents data for alpha-numeric data types.
 * 
 * @author James Watson
 */
public class CharData extends ValueData
{
//    private final Characters definition;
    private String data;
    
    public CharData(final Characters definition)
    {
        super(definition);
//        this.definition = definition;
    }
    
    /**
     * TODO - make this a rightTrim()
     * 
     * @return the trimmed String
     */
    public String getString()
    {
        return data == null ? "" : data.trim();
    }
    
    public Object getValue()
    {
        return getString();
    }
    
    protected void setValueImpl(Object data)
    {
        setValue((String) data, false);
    }
    
    /**
     * sets the data as a String
     * @param data
     */
//    public void setValue(String data)
//    {
//        setValue(data, true);
//    }
    
    public void setValue(String data, boolean validate)
    {
        if (validate) validate(data);
        this.data = data;
    }
    
    public String toString()
    {
        return getString();
    }

    public Object translate(String data)
    {
        return data;
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
        // todo make this a right-trim
        return this.getString();
    }

	@Override
	public <T> T toPOJO(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		Object v = null;
		
		if(clazz.equals(Integer.class)){
			v = Integer.getInteger((String)getValue());
		}else if(clazz.equals(Long.class)){
			v = Long.getLong((String)getValue());
		}else if(clazz.equals(Double.class)){
			v = Double.valueOf((String)getValue());
		}else if(clazz.equals(Boolean.class)){
			v = Boolean.valueOf((String)getValue());
		}else if(clazz.equals(BigDecimal.class)){
			v = new BigDecimal((String)getValue());
		} else{
			v = getValue();
		}
		
		return (T) v;
	}
}