package com.s8.io.joos.parsing;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjectsArrayScope extends ListScope {


	private OnParsed callback;
	
	private Class<?> componentType;
	
	private List<Object> values;
	
	
	public interface OnParsed {
		public void set(List<Object> values) throws JOOS_ParsingException;
	}
	

	public ObjectsArrayScope(Class<?> componentType, OnParsed callback) {
		super();
		this.callback = callback;
		this.componentType = componentType;
		this.values = new ArrayList<Object>();
	}
	
	
	public Class<?> getComponentType(){
		return componentType;
	}

	@Override
	public ParsingScope openItemScope() throws JOOS_ParsingException {
		return new ObjectScope(new ObjectScope.OnParsedObject() {
			public @Override void set(Object value) {
				values.add(value);
			}
		});
	}


	@Override
	public boolean isClosedBy(char c) {
		return c==']';
	}


	@Override
	public void close() throws JOOS_ParsingException {
		callback.set(values);
	}


	@Override
	public boolean isDefinable() {
		return false;
	}


	@Override
	public void define(String definition) {
		// node definition
	}

}