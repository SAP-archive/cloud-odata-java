package com.sap.core.odata.processor.core.jpa.access.model;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class EdmTypeConvertor {

  public static Class<?> convertToJavaType(final EdmType edmType) throws ODataJPAModelException, ODataJPARuntimeException {
    if (edmType instanceof EdmSimpleType) {
      return ((EdmSimpleType) edmType).getDefaultType();
    }
    throw ODataJPAModelException.throwException(ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(edmType.toString()), null);
  }
}
