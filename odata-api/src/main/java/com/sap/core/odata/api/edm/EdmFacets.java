package com.sap.core.odata.api.edm;


public interface EdmFacets {

  Boolean isNullable();

  String getDefaultValue();

  Integer getMaxLength();

  Boolean isFixedLength();

  Integer getPrecision();

  Integer getScale();

  Boolean isUnicode();

  String getCollation();

  EdmConcurrencyMode getConcurrencyMode();
}
