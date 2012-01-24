/*******************************************************************************
 * Copyright (c) 2012 MadRobot.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the GNU Lesser Public License v2.1
 *  which accompanies this distribution, and is available at
 *  http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *  
 *  Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/

package com.madrobot.di.wizard.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.madrobot.reflect.PrimitiveUtils;

/**
 * Mapper that allows a fully qualified class name to be replaced with an alias.
 *
 */
 class ClassAliasingMapper extends MapperWrapper {

    private final Map typeToName = new HashMap();
    private final Map classToName = new HashMap();
    private transient Map nameToType = new HashMap();

     ClassAliasingMapper(Mapper wrapped) {
        super(wrapped);
    }

    public void addClassAlias(String name, Class type) {
        nameToType.put(name, type.getName());
        classToName.put(type.getName(), name);
    }

    /**
     * @deprecated As of 1.3, method was a leftover of an old implementation
     */
    public void addClassAttributeAlias(String name, Class type) {
        addClassAlias(name, type);
    }

    public void addTypeAlias(String name, Class type) {
        nameToType.put(name, type.getName());
        typeToName.put(type, name);
    }

    public String serializedClass(Class type) {
        String alias = (String) classToName.get(type.getName());
        if (alias != null) {
            return alias;
        } else {
            for (final Iterator iter = typeToName.keySet().iterator(); iter.hasNext();) {
                final Class compatibleType = (Class)iter.next();
                if (compatibleType.isAssignableFrom(type)) {
                    return (String)typeToName.get(compatibleType);
                }
            }
            return super.serializedClass(type);
        }
    }

    public Class realClass(String elementName) {
        String mappedName = (String) nameToType.get(elementName);

        if (mappedName != null) {
            Class type = PrimitiveUtils.primitiveType(mappedName);
            if (type != null) {
                return type;
            }
            elementName = mappedName;
        }

        return super.realClass(elementName);
    }

    public boolean itemTypeAsAttribute(Class clazz) {
        return classToName.containsKey(clazz);
    }

    public boolean aliasIsAttribute(String name) {
        return nameToType.containsKey(name);
    }
    
    private Object readResolve() {
        nameToType = new HashMap();
        for (final Iterator iter = classToName.keySet().iterator(); iter.hasNext();) {
            final Object type = iter.next();
            nameToType.put(classToName.get(type), type);
        }
        for (final Iterator iter = typeToName.keySet().iterator(); iter.hasNext();) {
            final Class type = (Class)iter.next();
            nameToType.put(typeToName.get(type), type.getName());
        }
        return this;
    }
}
