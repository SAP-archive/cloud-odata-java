package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmSimpleType;

import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;

public class EdmSimpleTypeAdapter {

  private EdmSimpleType<?> edmSimpleType;
  private EdmSimpleTypeFacade facade;

  public EdmSimpleTypeAdapter(org.odata4j.edm.EdmSimpleType<?> edmSimpleType) {
    this.edmSimpleType = edmSimpleType;
    this.facade = new EdmSimpleTypeFacade();
  }

  public com.sap.core.odata.api.edm.EdmSimpleType getType() {
    if (EdmSimpleType.BINARY.equals(edmSimpleType))
      return facade.binaryInstance();
    else if (EdmSimpleType.BOOLEAN.equals(edmSimpleType))
      return facade.booleanInstance();
    else if (EdmSimpleType.BYTE.equals(edmSimpleType))
      return facade.byteInstance();
    else if (EdmSimpleType.DATETIME.equals(edmSimpleType))
      return facade.dateTimeInstance();
    else if (EdmSimpleType.DATETIMEOFFSET.equals(edmSimpleType))
      return facade.dateTimeOffsetInstance();
    else if (EdmSimpleType.DECIMAL.equals(edmSimpleType))
      return facade.decimalInstance();
    else if (EdmSimpleType.DOUBLE.equals(edmSimpleType))
      return facade.doubleInstance();
    else if (EdmSimpleType.GUID.equals(edmSimpleType))
      return facade.guidInstance();
    else if (EdmSimpleType.INT16.equals(edmSimpleType))
      return facade.int16Instance();
    else if (EdmSimpleType.INT32.equals(edmSimpleType))
      return facade.int32Instance();
    else if (EdmSimpleType.INT64.equals(edmSimpleType))
      return facade.int64Instance();
    else if (EdmSimpleType.SBYTE.equals(edmSimpleType))
      return facade.sByteInstance();
    else if (EdmSimpleType.SINGLE.equals(edmSimpleType))
      return facade.singleInstance();
    else if (EdmSimpleType.STRING.equals(edmSimpleType))
      return facade.stringInstance();
    else if (EdmSimpleType.TIME.equals(edmSimpleType))
      return facade.timeInstance();
    else
      return null;
  }
}
