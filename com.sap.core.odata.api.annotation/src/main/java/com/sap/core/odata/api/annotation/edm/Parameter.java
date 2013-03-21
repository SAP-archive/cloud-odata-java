package com.sap.core.odata.api.annotation.edm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
	enum Mode {
		IN, OUT, INOUT
	};

	String name();

	Mode mode() default Mode.IN;

	Facets facets() default @Facets;

	Documentation documentation() default @Documentation;
}
