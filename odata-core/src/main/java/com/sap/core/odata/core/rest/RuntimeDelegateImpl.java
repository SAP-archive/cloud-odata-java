package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.rest.ODataLocator;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade.EdmSimpleTypes;
import com.sap.core.odata.api.rest.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.rest.RuntimeDelegate;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBinary;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBit;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBoolean;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeByte;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDateTime;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDateTimeOffset;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDecimal;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDouble;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeGuid;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt16;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt32;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt64;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeNull;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeSingle;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeString;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeTime;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeUint7;

public class RuntimeDelegateImpl extends RuntimeDelegate {

  @Override
  public ODataResponseBuilder createODataResponseBuilder() {
    return new ODataResponseBuilderImpl();
  }

  @Override
  public Class<?> getExceptionMapper() {
    return ODataExceptionMapperImpl.class;
  }

  @Override
  public ODataLocator createODataLocator() {
    return new ODataLocatorImpl();
  }
    
    
  public EdmSimpleType getEdmSimpleType(EdmSimpleTypes edmSimpleType) {
    EdmSimpleType edmType = null;

    switch (edmSimpleType) {
    case BINARY:
      edmType = new EdmSimpleTypeBinary();
      break;
    case BOOLEAN:
      edmType = new EdmSimpleTypeBoolean();
      break;
    case BYTE:
      edmType = new EdmSimpleTypeByte();
      break;
    case DATETIME:
      edmType = new EdmSimpleTypeDateTime();
      break;
    case DATETIMEOFFSET:
      edmType = new EdmSimpleTypeDateTimeOffset();
      break;
    case DECIMAL:
      edmType = new EdmSimpleTypeDecimal();
      break;
    case DOUBLE:
      edmType = new EdmSimpleTypeDouble();
      break;
    case GUID:
      edmType = new EdmSimpleTypeGuid();
      break;
    case INT16:
      edmType = new EdmSimpleTypeInt16();
      break;
    case INT32:
      edmType = new EdmSimpleTypeInt32();
      break;
    case INT64:
      edmType = new EdmSimpleTypeInt64();
      break;
    case NULL:
      edmType = new EdmSimpleTypeNull();
      break;
    case SBYTE:
      edmType = new EdmSimpleTypeSByte();
      break;
    case SINGLE:
      edmType = new EdmSimpleTypeSingle();
      break;
    case STRING:
      edmType = new EdmSimpleTypeString();
      break;
    case TIME:
      edmType = new EdmSimpleTypeTime();
      break;
    case BIT:
      edmType = new EdmSimpleTypeBit();
      break;
    case UINT7:
      edmType = new EdmSimpleTypeUint7();
      break;
    default:
      throw new RuntimeException("Invalid Type " + edmSimpleType);
    }

    return edmType;
  }

}
