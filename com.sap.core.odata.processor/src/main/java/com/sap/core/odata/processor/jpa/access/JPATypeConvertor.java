package com.sap.core.odata.processor.jpa.access;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;

public class JPATypeConvertor {
	
	public static EdmSimpleTypeKind convertToEdmSimpleType(Class<?> jpaType){
		
		if (jpaType.equals(String.class))
			return EdmSimpleTypeKind.String;
		else if (jpaType.equals(Long.class) || jpaType.equals(long.class))
			return EdmSimpleTypeKind.Int64;
		else if (jpaType.equals(Short.class) || jpaType.equals(short.class))
			return EdmSimpleTypeKind.Int16;
		else if (jpaType.equals(Integer.class) || jpaType.equals(int.class))
			return EdmSimpleTypeKind.Int32;
		else if (jpaType.equals(Double.class) || jpaType.equals(double.class))
			return EdmSimpleTypeKind.Double;
		else if (jpaType.equals(Float.class) ||  jpaType.equals(float.class))
			return EdmSimpleTypeKind.Single;
		else if (jpaType.equals(BigDecimal.class))
			return EdmSimpleTypeKind.Decimal;
		else if (jpaType.equals(byte[].class))
			return EdmSimpleTypeKind.Binary;
		else if (jpaType.equals(Byte.class))
			return EdmSimpleTypeKind.Byte;
		else if (jpaType.equals(Byte[].class))
			return EdmSimpleTypeKind.Binary;
		else if (jpaType.equals(Boolean.class))
			return EdmSimpleTypeKind.Boolean;
		else if (jpaType.equals(Date.class))
			return EdmSimpleTypeKind.DateTime;
		else if (jpaType.equals(Calendar.class))
			return EdmSimpleTypeKind.DateTime;
		else if (jpaType.equals(UUID.class))
			return EdmSimpleTypeKind.Guid;
		
		return null;
	}
}
