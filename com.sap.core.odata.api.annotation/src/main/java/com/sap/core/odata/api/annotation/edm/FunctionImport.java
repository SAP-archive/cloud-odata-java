package com.sap.core.odata.api.annotation.edm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sap.core.odata.api.annotation.edmx.HttpMethod;
import com.sap.core.odata.api.annotation.edmx.HttpMethod.Name;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionImport {

	enum ReturnType {
		SCALAR, ENTITY_TYPE, COMPLEX_TYPE,NONE
	}

	enum Multiplicity {
		MANY, ONE
	}

	String name() default "";

	String entitySet() default "";

	ReturnType returnType();

	Multiplicity multiplicity() default Multiplicity.ONE;
	
	HttpMethod httpMethod( ) default @HttpMethod(name=Name.GET);
	
	Documentation documentation() default @Documentation;
}
