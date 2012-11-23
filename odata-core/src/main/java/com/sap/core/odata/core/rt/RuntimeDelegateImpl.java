package com.sap.core.odata.core.rt;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.ODataResponseBuilderImpl;
import com.sap.core.odata.core.edm.Bit;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmByte;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmNull;
import com.sap.core.odata.core.edm.EdmSByte;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.core.edm.Uint7;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.serializer.ODataSerialierFactory;
import com.sap.core.odata.core.uri.UriParserImpl;

public class RuntimeDelegateImpl extends RuntimeDelegate {

  @Override
  protected ODataResponseBuilder createODataResponseBuilder__() {
    return new ODataResponseBuilderImpl();
  }

  protected EdmSimpleType getEdmSimpleType__(EdmSimpleTypeKind edmSimpleType) {
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
  protected UriParser getUriParser__(Edm edm) {
    return new UriParserImpl(edm);
  }

  @Override
  protected EdmSimpleType getInternalEdmSimpleTypeByString__(String edmSimpleType) {
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
  protected EdmSimpleTypeFacade getSimpleTypeFacade__() {
    return new EdmSimpleTypeFacadeImpl();
  }

  @Override
  protected Edm createEdm__(EdmProvider provider) {
    return new EdmImplProv(provider);
  }

  @Override
  protected ODataSerializer createSerializer__(Format format) throws ODataSerializationException {
    return ODataSerialierFactory.create(format);
  }

}
