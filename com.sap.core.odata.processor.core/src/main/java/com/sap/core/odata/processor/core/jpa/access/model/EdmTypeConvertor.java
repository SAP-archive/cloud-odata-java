package com.sap.core.odata.processor.core.jpa.access.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class EdmTypeConvertor {

  public static Class<?> convertToJavaType(final EdmType edmType) throws ODataJPAModelException, ODataJPARuntimeException {
    if (edmType instanceof EdmSimpleType) {
        EdmSimpleType edmSimpleType = (EdmSimpleType) edmType;
        if (edmSimpleType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()) {
          return String.class;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance()) {
          return Long.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance()) {
          return Short.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance()) {
          return Integer.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()) {
          return Double.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
          return Float.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()) {
          return BigDecimal.class;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
          return byte[].class;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance()) {
          return Byte.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance()) {
          return Boolean.TYPE;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()) {
          return Date.class;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()) {
          return Calendar.class;
        }
        else if (edmSimpleType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()) {
          return UUID.class;
        }
    }
    throw ODataJPAModelException.throwException(ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(edmType.toString()), null);
  }
}
