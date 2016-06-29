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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.cb2java.copybook.Copybook;
import net.sf.cb2java.types.Group;

public class GroupData extends Data
{
    protected final Group definition;
    protected final List<Data> children;
    private final List<Data> wrapper;
    
    public GroupData(final Group definition, final List<Data> children)
    {
        super(definition);
        this.definition = definition;
        this.children = children;
        wrapper = Collections.unmodifiableList(children);
    }
    
//    private class Wrapper extends AbstractList
//    {
//        private Object data;
//        
//        public Object get(int index)
//        {
//            if (data == null) {
//                DataHolder holder = (DataHolder) children.get(index);
//                
//                data = holder.evaluate();
//            }
//            
//            return data;
//        }
//
//        public int size()
//        {
//            return children.size();
//        }
//    }
    
    @Override
    public boolean isLeaf()
    {
        return false;
    }
    
    /**
     * returns an immutable collection of children
     */
    @Override
    public List<Data> getChildren()
    {
        return wrapper;
    }
    
    /**
     * returns the first child with the specified name
     * irrespective of case
     * 
     * @param name the name of the child to look for
     * @return the first child with the given name
     * @throws IllegalArgumentException if no child is found
     */
    public Data getChild(String name)
    {
        for (Iterator i = wrapper.iterator(); i.hasNext();)
        {
            Data child = (Data) i.next();
            
            if (child.getName().equalsIgnoreCase(name)) return child;
        }
        
        return null;
    }
    /**
     * returns the first child with the specified name
     * irrespective of case
     * 
     * @param name the name of the child to look for
     * @return the first child with the given name
     * @throws IllegalArgumentException if no child is found
     */
    public List<Data> getChildList(String name)
    {
    	List<Data> ret = new ArrayList<Data>();
    	
        for (Iterator i = wrapper.iterator(); i.hasNext();)
        {
            Data child = (Data) i.next();
            
            if (child.getName().equalsIgnoreCase(name)) ret.add(child);
        }
        
        return ret;
    }

    /**
     * returns the first child from specified path
     * <br> <i>'BOOKROOT.GROUP-FIELD1.HEADER.FIELD-NAME'</i>
     * <br> <i>'BOOKROOT.GROUP-FIELD1.LIST[1].FIELD-NAME'</i>
     * 
     * @param name the name of the child to look for
     * @return the first child with the given name
     * @throws IllegalArgumentException if no child is found
     */
    public Data getChildFromPath(String path)
    {
    	String[] names = path.split("\\.");
    	Data data = this;
    	Pattern occour = Pattern.compile("([a-zA-Z0-9\\-]+)[ ]{0,}\\[[ ]{0,}([0-9]+)?[ ]{0,}\\]");
    	for (int i = 0; i < names.length; i++) {
    		String name = names[i];
    		int occourIndex = 0;
    		
    		Matcher m = occour.matcher(name);
    		if(m.matches()){
    			name = m.group(1);
    			occourIndex = Integer.parseInt(m.group(2));
    		}
    		
    		if(occourIndex>0){
    			if(data instanceof GroupData){
    				data = data == null ? getChildList(name).get(occourIndex-1) : ((GroupData) data).getChildList(name).get(occourIndex-1);
    			}
    		}else{
    			if(data instanceof GroupData){
    				data = data == null ? getChild(name) : ((GroupData) data).getChild(name);
    			}
    		}

    		if(i < names.length-1)  
			if(data==null) return null;
			
		}
        
        return data;
    }
    
    @Override
    public String toString()
    {
        return toString("");
    }
    
    @Override
    public String toString(String indent)
    {
        StringBuffer buffer = new StringBuffer(indent);
        
        buffer.append(getName());
        
        for (Iterator i = wrapper.iterator(); i.hasNext();) {
            buffer.append('\n');
            buffer.append(((Data) i.next()).toString(indent + INDENT));
        }
        
        return buffer.toString();
    }
    
    @Override
    public void write(OutputStream stream) throws IOException
    {
        for (Iterator i = wrapper.iterator(); i.hasNext();) {
            Data child = (Data) i.next();
            child.write(stream);
        }
    }

    /**
     * returns the children of this item
     */
    @Override
    public Object getValue()
    {
        return getChildren();
    }

    @Override
    public Object translate(String data)
    {
        throw new UnsupportedOperationException("cannot convert string to group");
    }
    
    /**
     * not supported
     */
    @Override
    protected void setValueImpl(Object data)
    {
        throw new IllegalArgumentException("operation not yet supported for groups");
    }

    /**
     * Convert the copybook group into a Java Map or List.
     * 
     * Single items (occurs == 1) are placed as entries in a <code>Map</code>. 
     * The Keys of a map are listed in the same order 
     * as the Copybook definition.
     * 
     * Multiple-occurs items are placed inside an immutable <code>List</code>.
     * 
     * @author github.com/devstopfix/cb2java
     * @return the copybook data as a map
     */
    @Override
    protected Object toPOJO() {
        Map<String, Object> group = new LinkedHashMap<String, Object>(this.wrapper.size());
        Iterator<Data> groupIterator = wrapper.iterator();
        while (groupIterator.hasNext()) {
            Data child = groupIterator.next();
            int occurs = child.getDefinition().getOccurs();
            if (occurs > 1) {
                List childOccurs = new ArrayList(occurs);
                childOccurs.add(child.toPOJO());
                for(int i=1; i<occurs;i++) {
                    childOccurs.add(groupIterator.next().toPOJO());
                }
                group.put(child.getName(), Collections.unmodifiableList(childOccurs));
            } else {
                group.put(child.getName(), child.toPOJO());
            }
        }
        
        return group;
    }

    
    @Override
    public <T> T toPOJO(Class<T> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        T ret = clazz.newInstance();
        HashMap<String, Field> retMap = Copybook.mapPojo(clazz);
        
        for (String path : retMap.keySet()) {
			Field field = retMap.get(path);
			Class<?> fieldType = field.getType(); 
			if (!field.isAccessible()) field.setAccessible(true);
			
			Data data = ((Data)getChildFromPath(path));
			
			field.set(ret, data.toPOJO(fieldType));
			
		}
        
        return ret;
    }


}