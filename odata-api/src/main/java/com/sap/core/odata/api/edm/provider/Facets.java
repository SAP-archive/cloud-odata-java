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

  public Boolean getNullable() {
    return nullable;
  }

  public Facets setNullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  public Facets setFixedLength(Boolean fixedLength) {
    this.fixedLength = fixedLength;
    return this;
  }

  public Facets setUnicode(Boolean unicode) {
    this.unicode = unicode;
    return this;
  }

  public Facets setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public Facets setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public Facets setPrecision(Integer precision) {
    this.precision = precision;
    return this;
  }

  public Facets setScale(Integer scale) {
    this.scale = scale;
    return this;
  }

  public Facets setCollation(String collation) {
    this.collation = collation;
    return this;
  }

  public Facets setConcurrencyMode(EdmConcurrencyMode concurrencyMode) {
    this.concurrencyMode = concurrencyMode;
    return this;
  }
  
  
}