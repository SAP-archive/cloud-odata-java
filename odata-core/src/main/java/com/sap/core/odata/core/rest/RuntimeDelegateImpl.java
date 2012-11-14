package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.RuntimeDelegate;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.edm.simpletype.Bit;
import com.sap.core.odata.core.edm.simpletype.EdmBinary;
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
import com.sap.core.odata.core.edm.simpletype.EdmNull;
import com.sap.core.odata.core.edm.simpletype.EdmSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.simpletype.EdmSingle;
import com.sap.core.odata.core.edm.simpletype.EdmString;
import com.sap.core.odata.core.edm.simpletype.EdmTime;
import com.sap.core.odata.core.edm.simpletype.Uint7;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriParserImpl;

public class RuntimeDelegateImpl extends RuntimeDelegate {

  @Override
  public ODataResponseBuilder createODataResponseBuilder() {
    return new ODataResponseBuilderImpl();
  }

  public EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    EdmSimpleType edmType = null;

    switch (edmSimpleType) {
    case Binary:
      edmType = EdmBinary.getInstance();
      break;
    case Boolean:
      edmType = EdmBoolean.getInstance();
      break;
    case Byte:
      edmType = EdmByte.getInstance();
      break;
    case DateTime:
      edmType = EdmDateTime.getInstance();
      break;
    case DateTimeOffset:
      edmType = EdmDateTimeOffset.getInstance();
      break;
    case Decimal:
      edmType = EdmDecimal.getInstance();
      break;
    case Double:
      edmType = EdmDouble.getInstance();
      break;
    case Guid:
      edmType = EdmGuid.getInstance();
      break;
    case Int16:
      edmType = EdmInt16.getInstance();
      break;
    case Int32:
      edmType = EdmInt32.getInstance();
      break;
    case Int64:
      edmType = EdmInt64.getInstance();
      break;
    case SByte:
      edmType = EdmSByte.getInstance();
      break;
    case Single:
      edmType = EdmSingle.getInstance();
      break;
    case String:
      edmType = EdmString.getInstance();
      break;
    case Time:
      edmType = EdmTime.getInstance();
      break;
    case Null:
      edmType = EdmNull.getInstance();
      break;
    default:
      throw new ODataRuntimeException("Invalid Type " + edmSimpleType);
    }

    return edmType;
  }

  @Override
  public UriParser getUriParser(Edm edm) {
    return new UriParserImpl(edm);
  }

  @Override
  public EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType) {
    EdmSimpleType edmType;

    if ("Bit".equals(edmSimpleType)) {
      edmType = Bit.getInstance();
    } else if ("Uint7".equals(edmSimpleType)) {
      edmType = Uint7.getInstance();
    } else {
      throw new ODataRuntimeException("Invalid internal Type " + edmSimpleType);
    }
    return edmType;
  }

  @Override
  public EdmSimpleTypeFacade getSimpleTypeFacade() {
    return new EdmSimpleTypeFacadeImpl();
  }

}
