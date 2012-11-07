package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmFacets;

public class Facets implements EdmFacets {

  Boolean nullable;
  String defaultValue;
  Integer maxLength;
  Boolean fixedLength;
  Integer precision;
  Integer scale;
  Boolean unicode;
  String collation;
  EdmConcurrencyMode concurrencyMode;

  public Facets(Boolean nullable, String defaultValue, Integer maxLength, Boolean fixedLength, Integer precision, Integer scale, Boolean unicode, String collation, EdmConcurrencyMode concurrencyMode) {
    super();
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

  public Boolean isNullable() {
    return nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public Boolean isFixedLength() {
    return fixedLength;
  }

  public Integer getPrecision() {
    return precision;
  }

  public Integer getScale() {
    return scale;
  }

  public Boolean isUnicode() {
    return unicode;
  }

  public String getCollation() {
    return collation;
  }

  public EdmConcurrencyMode getConcurrencyMode() {
    return concurrencyMode;
  }
}