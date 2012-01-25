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

import java.util.Iterator;

import com.madrobot.beans.ObjectIdDictionary;
import com.madrobot.di.wizard.xml.converters.ConversionException;
import com.madrobot.di.wizard.xml.converters.Converter;
import com.madrobot.di.wizard.xml.converters.ConverterLookup;
import com.madrobot.di.wizard.xml.converters.DataHolder;
import com.madrobot.di.wizard.xml.converters.MarshallingContext;
import com.madrobot.di.wizard.xml.core.MapBackedDataHolder;
import com.madrobot.di.wizard.xml.io.ExtendedHierarchicalStreamWriterHelper;
import com.madrobot.di.wizard.xml.io.HierarchicalStreamWriter;

class TreeMarshaller implements MarshallingContext {

	protected HierarchicalStreamWriter writer;
	protected ConverterLookup converterLookup;
	private Mapper mapper;
	private ObjectIdDictionary parentObjects = new ObjectIdDictionary();
	private DataHolder dataHolder;

	TreeMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
		this.writer = writer;
		this.converterLookup = converterLookup;
		this.mapper = mapper;
	}

	@Override
	public void convertAnother(Object item) {
		convertAnother(item, null);
	}

	@Override
	public void convertAnother(Object item, Converter converter) {
		if (converter == null) {
			converter = converterLookup.lookupConverterForType(item.getClass());
		} else {
			if (!converter.canConvert(item.getClass())) {
				ConversionException e = new ConversionException("Explicit selected converter cannot handle item");
				e.add("item-type", item.getClass().getName());
				e.add("converter-type", converter.getClass().getName());
				throw e;
			}
		}
		convert(item, converter);
	}

	protected void convert(Object item, Converter converter) {
		if (parentObjects.containsId(item)) {
			ConversionException e = new CircularReferenceException("Recursive reference to parent object");
			e.add("item-type", item.getClass().getName());
			e.add("converter-type", converter.getClass().getName());
			throw e;
		}
		parentObjects.associateId(item, "");
		converter.marshal(item, writer, this);
		parentObjects.removeId(item);
	}

	public void start(Object item, DataHolder dataHolder) {
		this.dataHolder = dataHolder;
		if (item == null) {
			writer.startNode(mapper.serializedClass(null));
			writer.endNode();
		} else {
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper.serializedClass(item.getClass()),
					item.getClass());
			convertAnother(item);
			writer.endNode();
		}
	}

	@Override
	public Object get(Object key) {
		lazilyCreateDataHolder();
		return dataHolder.get(key);
	}

	@Override
	public void put(Object key, Object value) {
		lazilyCreateDataHolder();
		dataHolder.put(key, value);
	}

	@Override
	public Iterator keys() {
		lazilyCreateDataHolder();
		return dataHolder.keys();
	}

	private void lazilyCreateDataHolder() {
		if (dataHolder == null) {
			dataHolder = new MapBackedDataHolder();
		}
	}

	protected Mapper getMapper() {
		return this.mapper;
	}

	public static class CircularReferenceException extends ConversionException {

		public CircularReferenceException(String msg) {
			super(msg);
		}
	}
}
