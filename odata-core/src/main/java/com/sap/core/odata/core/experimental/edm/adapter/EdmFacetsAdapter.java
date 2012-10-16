package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.core.edm.EdmConcurrencyMode;
import com.sap.core.odata.core.edm.EdmFacets;

public class EdmFacetsAdapter implements EdmFacets {

  private Boolean nullable;
  private String defaultValue;
  private Integer maxLength;
  private Boolean fixedLength;
  private Integer precision;
  private Integer scale;
  private Boolean unicode;
  private String collation;
  private EdmConcurrencyMode concurrencyMode;

  public EdmFacetsAdapter(Boolean nullable, String defaultValue, Integer maxLength, Boolean fixedLength, Integer precision, Integer scale, Boolean unicode, String collation, EdmConcurrencyMode concurrencyMode) {
    this.nullable = nullable;
    this.defaultValue = defaultValue;
    this.maxLength = maxLength;
    this.fixedLength = fixedLength;
    this.precision = precision;
    this.scale = scale;
    this.unicode = unicode;
    this.collation = collation;
    this.concurrencyMode = concurrencyMode;
  }

  @Override
  public Boolean isNullable() {
    return this.nullable;
  }

  @Override
  public String getDefaultValue() {
    return this.defaultValue;
  }

  @Override
  public Integer getMaxLength() {
    return this.maxLength;
  }

  @Override
  public Boolean isFixedLength() {
    return this.fixedLength;
  }

  @Override
  public Integer getPrecision() {
    return this.precision;
  }

  @Override
  public Integer getScale() {
    return this.scale;
  }

  @Override
  public Boolean isUnicode() {
    return this.unicode;
  }

  @Override
  public String getCollation() {
    return this.collation;
  }

  @Override
  public EdmConcurrencyMode getConcurrencyMode() {
    return this.concurrencyMode;
  }

}
