package com.sap.core.odata.api.annotation.edmx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface HttpMethod {
	enum Name{
		POST,
		PUT,
		GET,
		MERGE,
		DELETE,
		PATCH
	};
	
	Name name( );
}
