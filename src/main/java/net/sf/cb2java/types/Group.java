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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.GroupData;

/**
 * Type used to represent an element with child elements
 */
public class Group extends Element
{
    /** the list of children */
    private final List<Element> children = new ArrayList();
    /** the wrapper list exposed through getChildren() */
    private final List<Element> wrapper = Collections.unmodifiableList(children);
    
    public Group(final String name, final int level, final int occurs)
    {
        super(name, level, occurs);
    }
    
    public void addChild(Element element)
    {
        children.add(element);
        element.setParent(this);
    }
    
    /**
     * returns an immutable list of the children in this group.
     */
    @Override
    public List<Element> getChildren()
    {
        return wrapper;
    }

    @Override
    public int getLength()
    {
        int length = 0;
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                length += element.getLength();
            }
        }
        
        return length;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(super.toString());
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            buffer.append(i.next());
        }
        
        return buffer.toString();
    }

    public Data create()
    {
        ArrayList<Data> dataChildren = new ArrayList<Data>();
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                dataChildren.add(element.create());
            }
        }
        
        return new GroupData(this, dataChildren);
    }
    
    public Data parse(final byte[] bytes)
    {
        ArrayList dataChildren = new ArrayList();
        
        int pos = 0;
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            final Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                final int end = pos + element.getLength();
                dataChildren.add(element.parse(sub(bytes, pos, end)));
                
                pos = end;
            }
        }
        
        return new GroupData(this, dataChildren);
    }

    private byte[] sub(byte[] in, int start, int end)
    {
        byte[] out = new byte[end - start];
        
//        System.out.println("sub: " + in.length + ", " + start + ", " + end);
        
        System.arraycopy(in, start, out, 0, out.length);
        
        return out;
    }
    
    public byte[] toBytes(Object data)
    {
        throw new IllegalArgumentException("cannot read bytes from a group");
    }

    public void validate(Object data)
    {
        throw new IllegalArgumentException("groups do not accept data");
    }

    @Override
    public void setValue(Value value)
    {
        throw new RuntimeException("groups cannot have a value");
    }
    
    @Override
    public Value getValue()
    {
        throw new RuntimeException("groups cannot have a value");
    }
}