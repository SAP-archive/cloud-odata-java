package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade.EdmSimpleTypeKind;
import com.sap.core.odata.api.rest.ODataLocator;
import com.sap.core.odata.api.rest.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.rest.RuntimeDelegate;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.edm.simpletype.EdmBinary;
import com.sap.core.odata.core.edm.simpletype.EdmBit;
import com.sap.core.odata.core.edm.simpletype.EdmBoolean;
import com.sap.core.odata.core.edm.simpletype.EdmByte;
import com.sap.core.odata.core.edm.simpletype.EdmDateTime;
import com.sap.core.odata.core.edm.simpletype.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.simpletype.EdmDecimal;
import com.sap.core.odata.core.edm.simpletype.EdmDouble;
import com.sap.core.odata.core.edm.simpletype.EdmGuid;
import com.sap.core.odata.core.edm.simpletype.EdmInt16;
import com.sap.core.odata.core.edm.simpletype.EdmInt32;
import com.sap.core.odata.core.edm.simpletype.EdmInt64;
import com.sap.core.odata.core.edm.simpletype.EdmSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSingle;
import com.sap.core.odata.core.edm.simpletype.EdmString;
import com.sap.core.odata.core.edm.simpletype.EdmTime;
import com.sap.core.odata.core.edm.simpletype.EdmUint7;
import com.sap.core.odata.core.uri.UriParserImpl;

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

  public EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    EdmSimpleType edmType = null;

    switch (edmSimpleType) {
    case BINARY:
      edmType = new EdmBinary();
      break;
    case BOOLEAN:
      edmType = new EdmBoolean();
      break;
    case BYTE:
      edmType = new EdmByte();
      break;
    case DATETIME:
      edmType = new EdmDateTime();
      break;
    case DATETIMEOFFSET:
      edmType = new EdmDateTimeOffset();
      break;
    case DECIMAL:
      edmType = new EdmDecimal();
      break;
    case DOUBLE:
      edmType = new EdmDouble();
      break;
    case GUID:
      edmType = new EdmGuid();
      break;
    case INT16:
      edmType = new EdmInt16();
      break;
    case INT32:
      edmType = new EdmInt32();
      break;
    case INT64:
      edmType = new EdmInt64();
      break;
    case SBYTE:
      edmType = new EdmSByte();
      break;
    case SINGLE:
      edmType = new EdmSingle();
      break;
    case STRING:
      edmType = new EdmString();
      break;
    case TIME:
      edmType = new EdmTime();
      break;
    case BIT:
      edmType = new EdmBit();
      break;
    case UINT7:
      edmType = new EdmUint7();
      break;
    default:
      throw new RuntimeException("Invalid Type " + edmSimpleType);
    }

    return edmType;
  }

  @Override
  public UriParser getUriParser(Edm edm) {
    return new UriParserImpl(edm);
  }

}
