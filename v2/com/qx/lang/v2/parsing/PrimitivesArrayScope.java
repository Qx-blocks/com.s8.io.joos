package com.qx.lang.v2.parsing;

import java.lang.reflect.Array;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.ScopeType;

public class PrimitivesArrayScope extends ParsingScope {
	
	public abstract static class Enclosing {

		public abstract void set(Object value) throws IllegalArgumentException, IllegalAccessException;
	}


	private abstract class ArrayHandler {
		public abstract void set(int index, String value);
	}

	
	private class BooleanArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setBoolean(array, index, Boolean.valueOf(value));
		}
	}
	
	private class ShortArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setShort(array, index, Short.valueOf(value));
		}
	}
	
	private class IntegerArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setInt(array, index, Integer.valueOf(value));
		}
	}
	
	private class LongArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setLong(array, index, Long.valueOf(value));
		}
	}
	
	private class FloatArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setFloat(array, index, Float.valueOf(value));
		}
	}
	
	private class DoubleArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.setDouble(array, index, Double.valueOf(value));
		}
	}
	
	private class StringArrayHandler extends ArrayHandler {		
		public @Override void set(int index, String value) {
			Array.set(array, index, value);
		}
	}
	
	private Enclosing enclosing;
	
	private Class<?> componentType;
	
	private Object array;
	
	private ArrayHandler handler;


	public PrimitivesArrayScope(Enclosing enclosing, Class<?> componentType) throws Ws3dParsingException {
		super();
		this.enclosing = enclosing;
		this.componentType = componentType;
	
		if(componentType==boolean.class) {
			handler = new BooleanArrayHandler();
		}
		else if(componentType==short.class) {
			handler = new ShortArrayHandler();
		}
		else if(componentType==int.class) {
			handler = new IntegerArrayHandler();
		}
		else if(componentType==long.class) {
			handler = new LongArrayHandler();
		}
		else if(componentType==float.class) {
			handler = new FloatArrayHandler();
		}
		else if(componentType==double.class) {
			handler = new DoubleArrayHandler();
		}
		else if(componentType==String.class) {
			handler = new StringArrayHandler();
		}
		else{
			throw new Ws3dParsingException("Unsupported primitive type: "+componentType.getName());
		}	
	}
	

	public void define(int length) throws Ws3dParsingException {
		
		array = Array.newInstance(componentType, length);
		
		try {
			enclosing.set(array);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new Ws3dParsingException(e.getMessage());
		}
	}
	
	@Override
	public ParsingScope enter(String declarator) throws Ws3dParsingException {
		
		int index = Integer.valueOf(declarator);
		
		return new PrimitiveScope(new PrimitiveScope.Enclosing() {
			
			@Override
			public void set(String value) throws Ws3dParsingException {
				handler.set(index, value);
			}
		});
	}

	
	@Override
	public ScopeType getType() {
		return ScopeType.PRIMITIVES_ARRAY;
	}


	@Override
	public boolean isClosedBy(char c) {
		return c==']';
	}
}
