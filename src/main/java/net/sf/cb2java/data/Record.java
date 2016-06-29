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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.cb2java.copybook.Copybook;

public class Record extends GroupData
{
    public Record(String name, GroupData data)
    {
        super(data.definition, data.children);
    }
    
    /**
     * Convert the copybook data types into standard Java structures
     * and objects.
     * 
     * <li>Groups become Maps with keys ordered by the Copybook definition
     * <li>Occurs use Lists
     * <li>PICX become Strings
     * <li>PIC9 become Integers or BigDecimals
     * 
     * The result should be considered immutable, any modifications made
     * are no applied to the parent <code>Record</code>.
     * 
     * @author github.com/devstopfix/cb2java
     * @return the copybook data as Plain Java Objects
     */
    public Map<String,Object> toMap() {
        Map<String, Object> group = new LinkedHashMap<String, Object>(getChildren().size());
        for(Data child: getChildren()) {
            group.put(child.getName(), child.toPOJO());
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
			
			Data data = ((Data)getChildFromPath(path));
			
			if (!field.isAccessible()) field.setAccessible(true);
			
			if(!fieldType.isAssignableFrom(List.class)){
				field.set(ret, data.toPOJO(fieldType));
				continue;
			}
			
				
			List<Object> listValue = (List<Object>) field.get(ret);
			if(listValue==null) {
				listValue = new ArrayList<Object>();
				field.set(ret, listValue);
			}
			
			Type genericType = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
			Class<?> genericClass = Class.forName(genericType.getTypeName());
			for (int i = 0; i < data.getDefinition().getOccurs(); i++) {
				data = ((Data)getChildFromPath(path + "[" + (i + 1) + "]"));
				listValue.add(data.toPOJO(genericClass));
			}

				
		}
        
        return ret;
    }
    
}