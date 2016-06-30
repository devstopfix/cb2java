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

import java.util.Collections;
import java.util.List;
import net.sf.cb2java.types.Element;

public abstract class ValueData extends Data
{    
    public ValueData(Element definition)
    {
        super(definition);
    }

    public boolean isLeaf()
    {
        return true;
    }
    
    public List<Data> getChildren()
    {
        return Collections.EMPTY_LIST;
    }
   
    @Override
    public abstract String toString();
   
    public String toString(String indent)
    {
        return indent + toString();
    }
}