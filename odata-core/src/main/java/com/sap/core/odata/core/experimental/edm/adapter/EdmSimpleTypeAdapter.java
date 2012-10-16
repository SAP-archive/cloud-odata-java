package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmSimpleType;

public class EdmSimpleTypeAdapter {

  private EdmSimpleType<?> edmSimpleType;

  public EdmSimpleTypeAdapter(org.odata4j.edm.EdmSimpleType<?> edmSimpleType) {
    this.edmSimpleType = edmSimpleType;
  }

  public com.sap.core.odata.core.edm.EdmSimpleType getType() {
    if (EdmSimpleType.BINARY.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.BINARY;
    else if (EdmSimpleType.BOOLEAN.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.BOOLEAN;
    else if (EdmSimpleType.BYTE.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.BYTE;
    else if (EdmSimpleType.DATETIME.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.DATETIME;
    else if (EdmSimpleType.DATETIMEOFFSET.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.DATETIMEOFFSET;
    else if (EdmSimpleType.DECIMAL.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.DECIMAL;
    else if (EdmSimpleType.DOUBLE.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.DOUBLE;
    else if (EdmSimpleType.GUID.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.GUID;
    else if (EdmSimpleType.INT16.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.INT16;
    else if (EdmSimpleType.INT32.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.INT32;
    else if (EdmSimpleType.INT64.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.INT64;
    else if (EdmSimpleType.SBYTE.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.SBYTE;
    else if (EdmSimpleType.SINGLE.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.SINGLE;
    else if (EdmSimpleType.STRING.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.STRING;
    else if (EdmSimpleType.TIME.equals(edmSimpleType))
      return com.sap.core.odata.core.edm.EdmSimpleType.TIME;
    else
      return null;
  }
}
