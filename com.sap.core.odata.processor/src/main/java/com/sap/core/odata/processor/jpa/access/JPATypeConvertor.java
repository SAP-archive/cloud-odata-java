package com.sap.core.odata.processor.jpa.access;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

/**
 * This class holds utility methods for Type conversions between JPA and OData Types.
 * 
 * @author SAP AG
 *
 */
public class JPATypeConvertor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JPATypeConvertor.class);
	
	/**
	 * This utility method converts a given jpa Type to equivalent
	 * EdmSimpleTypeKind for maintaining compatibility between Java and OData
	 * Types.
	 * 
	 * @param jpaType
	 *            The JPA Type input.
	 * @return The corresponding EdmSimpleTypeKind.
	 * @throws ODataJPAModelException
	 * 
	 * @see EdmSimpleTypeKind
	 */
	public static EdmSimpleTypeKind convertToEdmSimpleType(Class<?> jpaType) throws ODataJPAModelException{
		if (jpaType.equals(String.class)){
			//logger.debug("Returned EdmSimpleTypeKind.String For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.String;
		}
		else if (jpaType.equals(Long.class) || jpaType.equals(long.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Int64 For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Int64;
		}
		else if (jpaType.equals(Short.class) || jpaType.equals(short.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Int16 For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Int16;
		}
		else if (jpaType.equals(Integer.class) || jpaType.equals(int.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Int32 For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Int32;
		}
		else if (jpaType.equals(Double.class) || jpaType.equals(double.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Double For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Double;
		}
		else if (jpaType.equals(Float.class) ||  jpaType.equals(float.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Single For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Single;
		}
		else if (jpaType.equals(BigDecimal.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Decimal For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Decimal;
		}
		else if (jpaType.equals(byte[].class)){
			//logger.debug("Returned EdmSimpleTypeKind.Binary For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Binary;
		}
		else if (jpaType.equals(Byte.class) ||  jpaType.equals(byte.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Byte For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Byte;
		}
		else if (jpaType.equals(Byte[].class)){
			//logger.debug("Returned EdmSimpleTypeKind.Binary For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Binary;
		}
		else if (jpaType.equals(Boolean.class) ||  jpaType.equals(boolean.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Boolean For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Boolean;
		}
		else if (jpaType.equals(Date.class)){
			//logger.debug("Returned EdmSimpleTypeKind.DateTime For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.DateTime;
		}
		else if (jpaType.equals(Calendar.class)){
			//logger.debug("Returned EdmSimpleTypeKind.DateTime For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.DateTime;
		}
		else if (jpaType.equals(UUID.class)){
			//logger.debug("Returned EdmSimpleTypeKind.Guid For JPAType - {}",jpaType.toString());
			return EdmSimpleTypeKind.Guid;
		}
		LOGGER.error("Encountered {} for JPAType - {}",ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(jpaType.toString(),jpaType.toString()));
		throw ODataJPAModelException.throwException(ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(jpaType.toString()),null);
	}
}
