package com.sap.core.odata.core.edm;

public interface EdmFacets {

  public Boolean isNullable();

  public String getDefaultValue();

  public Integer getMaxLength();

  public Boolean isFixedLength();

  public Integer getPrecision();

  public Integer getScale();

  public Boolean isUnicode();

  public String getCollation();

  public EdmConcurrencyMode getConcurrencyMode();
}
