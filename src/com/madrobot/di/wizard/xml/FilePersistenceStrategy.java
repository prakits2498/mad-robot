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

import java.io.File;

import com.madrobot.di.wizard.xml.converters.Converter;
import com.madrobot.di.wizard.xml.converters.SingleValueConverter;
import com.madrobot.di.wizard.xml.io.StreamException;
import com.madrobot.di.wizard.xml.io.xml.DomDriver;


/**
 * PersistenceStrategy to assign keys with single value to objects persisted in files. The
 * default naming strategy is based on the key's type and its {@link SingleValueConverter}. It
 * escapes all characters that are normally illegal in the most common file systems. Such a
 * character is escaped with percent escaping as it is done by URL encoding. The XMLWizard used to
 * marshal the values is also requested for the key's SingleValueConverter. A
 * {@link StreamException} is thrown if no such converter is registered.
 * 
 * @since 1.3.1
 */
 class FilePersistenceStrategy extends AbstractFilePersistenceStrategy {

    private final String illegalChars;

    /**
     * Create a new FilePersistenceStrategy. Use a standard XStream instance with a
     * {@link DomDriver}.
     * 
     * @param baseDirectory the directory for the serialized values
     * @since 1.3.1
     */
    public FilePersistenceStrategy(final File baseDirectory) {
        this(baseDirectory, new XMLWizard(new DomDriver()));
    }

    /**
     * Create a new FilePersistenceStrategy with a provided XStream instance.
     * 
     * @param baseDirectory the directory for the serialized values
     * @param xstream the XStream instance to use for (de)serialization
     * @since 1.3.1
     */
    public FilePersistenceStrategy(final File baseDirectory, final XMLWizard xstream) {
        this(baseDirectory, xstream, "utf-8", "<>?:/\\\"|*%");
    }

    /**
     * Create a new FilePersistenceStrategy with a provided XStream instance and the characters
     * to encode.
     * 
     * @param baseDirectory the directory for the serialized values
     * @param xmlWizard the XMLWizard instance to use for (de)serialization
     * @param encoding encoding used to write the files
     * @param illegalChars illegal characters for file names (should always include '%' as long
     *            as you do not overwrite the (un)escape methods)
     * @since 1.3.1
     */
    public FilePersistenceStrategy(
        final File baseDirectory, final XMLWizard xmlWizard, final String encoding,
        final String illegalChars) {
        super(baseDirectory, xmlWizard, encoding);
        this.illegalChars = illegalChars;
    }

    protected boolean isValid(final File dir, final String name) {
        return super.isValid(dir, name) && name.indexOf('@') > 0;
    }

    /**
     * Given a filename, the unescape method returns the key which originated it.
     * 
     * @param name the filename
     * @return the original key
     */
    protected Object extractKey(final String name) {
        final String key = unescape(name.substring(0, name.length() - 4));
        if ("null@null".equals(key)) {
            return null;
        }
        int idx = key.indexOf('@');
        if (idx < 0) {
            throw new StreamException("Not a valid key: " + key);
        }
        Class type = getMapper().realClass(key.substring(0, idx));
        Converter converter = getConverterLookup().lookupConverterForType(type);
        if (converter instanceof SingleValueConverter) {
            final SingleValueConverter svConverter = (SingleValueConverter)converter;
            return svConverter.fromString(key.substring(idx + 1));
        } else {
            throw new StreamException("No SingleValueConverter for type "
                + type.getName()
                + " available");
        }
    }

    protected String unescape(String name) {
        final StringBuffer buffer = new StringBuffer();
        for (int idx = name.indexOf('%'); idx >= 0; idx = name.indexOf('%')) {
            buffer.append(name.substring(0, idx));
            int c = Integer.parseInt(name.substring(idx + 1, idx + 3), 16);
            buffer.append((char)c);
            name = name.substring(idx + 3);
        }
        buffer.append(name);
        return buffer.toString();
    }

    /**
     * Given a key, the escape method returns the filename which shall be used.
     * 
     * @param key the key
     * @return the desired and escaped filename
     */
    protected String getName(final Object key) {
        if (key == null) {
            return "null@null.xml";
        }
        Class type = key.getClass();
        Converter converter = getConverterLookup().lookupConverterForType(type);
        if (converter instanceof SingleValueConverter) {
            final SingleValueConverter svConverter = (SingleValueConverter)converter;
            return getMapper().serializedClass(type)
                + '@'
                + escape(svConverter.toString(key))
                + ".xml";
        } else {
            throw new StreamException("No SingleValueConverter for type "
                + type.getName()
                + " available");
        }
    }

    protected String escape(final String key) {
        final StringBuffer buffer = new StringBuffer();
        final char[] array = key.toCharArray();
        for (int i = 0; i < array.length; i++ ) {
            final char c = array[i];
            if (c >= ' ' && illegalChars.indexOf(c) < 0) {
                buffer.append(c);
            } else {
                buffer.append("%" + Integer.toHexString(c).toUpperCase());
            }
        }
        return buffer.toString();
    }
}
