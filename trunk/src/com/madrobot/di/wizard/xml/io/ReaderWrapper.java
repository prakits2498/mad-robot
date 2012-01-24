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
 
package com.madrobot.di.wizard.xml.io;

import java.util.Iterator;

import com.madrobot.di.wizard.xml.converters.ErrorWriter;

/**
 * Base class to make it easy to create wrappers (decorators) for HierarchicalStreamReader.
 *
 * @author Joe Walnes
 */
public abstract class ReaderWrapper implements ExtendedHierarchicalStreamReader {

    protected HierarchicalStreamReader wrapped;

    protected ReaderWrapper(HierarchicalStreamReader reader) {
        this.wrapped = reader;
    }

    public boolean hasMoreChildren() {
        return wrapped.hasMoreChildren();
    }

    public void moveDown() {
        wrapped.moveDown();
    }

    public void moveUp() {
        wrapped.moveUp();
    }

    public String getNodeName() {
        return wrapped.getNodeName();
    }

    public String getValue() {
        return wrapped.getValue();
    }

    public String getAttribute(String name) {
        return wrapped.getAttribute(name);
    }

    public String getAttribute(int index) {
        return wrapped.getAttribute(index);
    }

    public int getAttributeCount() {
        return wrapped.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return wrapped.getAttributeName(index);
    }

    public Iterator getAttributeNames() {
        return wrapped.getAttributeNames();
    }

    public void appendErrors(ErrorWriter errorWriter) {
        wrapped.appendErrors(errorWriter);
    }

    public void close() {
        wrapped.close();
    }

    public String peekNextChild() {
        if (! (wrapped instanceof ExtendedHierarchicalStreamReader)) {
            throw new UnsupportedOperationException("peekNextChild");
        }
        return ((ExtendedHierarchicalStreamReader)wrapped).peekNextChild();
    }

    public HierarchicalStreamReader underlyingReader() {
        return wrapped.underlyingReader();
    }
}
