package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;

public class JPATypeConvertor {
	
	public static EdmSimpleTypeKind convertToEdmSimpleType(Class<?> jpaType){
		
		if (jpaType.equals(String.class))
			return EdmSimpleTypeKind.String;
		else if (jpaType.equals(Long.class))
			return EdmSimpleTypeKind.Int64;
		else if (jpaType.equals(Short.class))
			return EdmSimpleTypeKind.Int16;
		else if (jpaType.equals(Integer.class))
			return EdmSimpleTypeKind.Int32;
		
		return null;
	}
}
