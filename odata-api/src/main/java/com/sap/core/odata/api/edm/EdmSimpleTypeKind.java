package com.sap.core.odata.api.edm;

public enum EdmSimpleTypeKind
{
  BINARY("Binary"), BOOLEAN("Boolean"), BYTE("Byte"), DATETIME("DateTime"), DATETIMEOFFSET("DateTimeOffset"), DECIMAL("Decimal"), DOUBLE("Double"), GUID("Guid"), INT16("Int16"), INT32("Int32"), INT64("Int64"), SBYTE("SByte"), SINGLE("Single"), STRING("String"), TIME("Time"), BIT("Bit"), UINT7("UInt7");

  private String name;

  private EdmSimpleTypeKind(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
