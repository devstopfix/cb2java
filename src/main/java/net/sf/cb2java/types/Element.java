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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import net.sf.cb2java.Settings;
import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;

/**
 * base class for types.
 * 
 * @author James Watson
 */
public abstract class Element {
	
    /** the name of the element from the copybook */
    private final String name;
    /** the level of the element from the copybook */
    private final int level;
    /** how many times the element occurs in the application data */
    private final int occurs;
    /** the absolute position of the where this item starts in data */
    private int position;
    /** the instance that represents the data that defines this element */
    private Settings settings;
    /** the default value of this element */
    private Value value;
    /** the parent of this element */
    private Group parent;
    
    /**
     * constructor
     * 
     * @param name the element name
     * @param level the level of the element
     * @param occurs how many times the element occurs
     */
    protected Element(final String name, final int level, final int occurs) {
        this.name = name;
        this.level = level;
        this.occurs = occurs;
    }
    
    /**
     * the value for this element.  This value determines
     * how empty bytes are filled and what the default
     * for a data element will be.
     * 
     * @return the value set for this element or the default
     */
    public Value getValue() {
        return value;
    }
    
    /**
     * gets the children of this element or null if there are none
     * 
     * @return the children of this element or null if there are none
     */
    public abstract List<Element> getChildren();
    
    /**
     * returns the number of bytes of one instance of this element
     * 
     * @return the number of bytes of one instance of this element
     */
    public abstract int getLength();
    
    /**
     * creates a new empty Data instance for this element
     * 
     * @return a new empty Data instance for this element
     */
    public abstract Data create();
    
    /**
     * creates a new empty Data instance from the data supplied
     * 
     * @param input the input data
     * @return a new empty Data instance from the data supplied
     */
    public abstract Data parse(byte[] input);
    
    /**
     * validates the data based on this element definition
     * 
     * @param data the data to validate
     * @throws IllegalArgumentException if the data is invalid
     */
    public abstract void validate(Object data) throws IllegalArgumentException;
    
    /**
     * converts the supplied data to bytes
     * 
     * @param data the data to convert to bytes
     * @return the bytes for the data
     */
    public abstract byte[] toBytes(Object data);
    
    /**
     * returns the name of this element
     * 
     * @return the name of this element
     */
    public final String getName() {
        return name;
    }
    
    /**
     * returns the level of this element
     * 
     * @return the level of this element
     */
    public final int getLevel() {
        return level;
    }
    
    /**
     * returns the position of this element
     * 
     * @return the position of this element
     */
    public final int getPosition() {
        return position;
    }
    
    /**
     * returns the number of times this item appears in data
     * 
     * @return the number of times this item appears in data
     */
    public final int getOccurs() {
        return occurs;
    }
    
    /**
     * sets the value for this element that is used to
     * fill in empty bytes.  The default value a data
     * element will be filled solely with these bytes.
     * 
     * @param value
     */
    public void setValue(Value value) {
        this.value = value;
    }
    
    /**
     * writes the data as bytes to the given stream
     * 
     * @param stream the outputstream
     * @param data the data to write as bytes
     * @throws IOException
     */
    public final void write(OutputStream stream, Object data) throws IOException {
        validate(data);
        stream.write(toBytes(data));
    }
    
    /**
     * helper method for converting the given bytes to a string with
     * the parent copybook's encoding
     * 
     * @param data the data to convert to a String
     * @return the String value
     */
    public final String getString(byte[] data) {
        try {
            return new String(data, getSettings().getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * converts a String to a byte array based on the current encoding
     * 
     * @param s the string to convert
     * @return the bytes for the string
     */
    public final byte[] getBytes(String s) {
        try {
            return s.getBytes(getSettings().getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sets the settings for the element and all child elements
     * 
     * @param settings the new settings
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * returns the parent of this element
     * 
     * @return the parent of this element
     */
    public Group getParent(){
        return parent;
    }
    
    /**
     * Sets the parent for this element
     * 
     * @param parent the parent for this element
     */
    public void setParent(Group parent){
        this.parent = parent;
    }
    
    /**
     * Returns the settings for this element
     * 
     * @return the settings for this element
     */
    public Settings getSettings() {
        if (settings != null) {
            return settings;
        } else if (getParent() != null) {
            return getParent().getSettings();
        } else {
            return Settings.DEFAULT();
        }
    }
    
    @Override
    public String toString() {
        return new String(getSettings().getValues().SPACES.fill(level)) + name + ": '" 
            + this.getClass() + " " + getLength() + "'\n";
    }
}