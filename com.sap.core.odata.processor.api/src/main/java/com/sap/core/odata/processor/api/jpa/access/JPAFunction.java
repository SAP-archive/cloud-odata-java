package com.sap.core.odata.processor.api.jpa.access;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JPAFunction {

	private Method function;
	private Type[] parameterTypes;
	private Type returnType;

	public Method getFunction() {
		return function;
	}

	public Type[] getParameterTypes() {
		return parameterTypes;
	}

	public Type getReturnType() {
		return returnType;
	}

}
