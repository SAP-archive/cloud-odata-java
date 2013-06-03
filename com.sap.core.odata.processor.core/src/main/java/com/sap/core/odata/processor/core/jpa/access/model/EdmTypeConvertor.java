package com.sap.core.odata.processor.core.jpa.access.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmByte;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class EdmTypeConvertor {

  public static Class<?> convertToJavaType(final EdmType edmType) throws ODataJPAModelException, ODataJPARuntimeException {
    if (edmType instanceof EdmString) {
      return String.class;
    }
    else if (edmType instanceof EdmInt64) {
      return Long.TYPE;
    }
    else if (edmType instanceof EdmInt16) {
      return Short.TYPE;
    }
    else if (edmType instanceof EdmInt32) {
      return Integer.TYPE;
    }
    else if (edmType instanceof EdmDouble) {
      return Double.TYPE;
    }
    else if (edmType instanceof EdmSingle) {
      return Float.TYPE;
    }
    else if (edmType instanceof EdmDecimal) {
      return BigDecimal.class;
    }
    else if (edmType instanceof EdmBinary) {
      return byte[].class;
    }
    else if (edmType instanceof EdmByte) {
      return Byte.TYPE;
    }
    else if (edmType instanceof EdmBoolean /*||  edmType instanceof (boolean.class)*/) {
      return Boolean.TYPE;
    }
    else if (edmType instanceof EdmDateTime) {
      return Date.class;
    }
    else if (edmType instanceof EdmTime) {
      return Calendar.class;
    }
    else if (edmType instanceof EdmGuid) {
      return UUID.class;
    }
    throw ODataJPAModelException.throwException(ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(edmType.toString()), null);
  }
}
