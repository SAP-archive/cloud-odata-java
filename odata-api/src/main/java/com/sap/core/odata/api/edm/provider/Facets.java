package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmFacets;

/**
 * Objects of this class represent the facets a entity type, property or function import can have
 * @author SAP AG
 */
public class Facets implements EdmFacets {

  /** Specification default is TRUE but we wont set it here because 
   * we want to know if its set explicitly by an application */
  Boolean nullable;
  String defaultValue;
  Integer maxLength;
  Boolean fixedLength;
  Integer precision;
  Integer scale;
  Boolean unicode;
  String collation;
  EdmConcurrencyMode concurrencyMode;

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#isNullable()
   */
  public Boolean isNullable() {
    return nullable;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getDefaultValue()
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getMaxLength()
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#isFixedLength()
   */
  public Boolean isFixedLength() {
    return fixedLength;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getPrecision()
   */
  public Integer getPrecision() {
    return precision;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getScale()
   */
  public Integer getScale() {
    return scale;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#isUnicode()
   */
  public Boolean isUnicode() {
    return unicode;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getCollation()
   */
  public String getCollation() {
    return collation;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmFacets#getConcurrencyMode()
   */
  public EdmConcurrencyMode getConcurrencyMode() {
    return concurrencyMode;
  }

  /**
   * @return <b>boolean</b> if this {@link Facets} is nullable
   */
  public Boolean getNullable() {
    return nullable;
  }

  /**
   * Sets if this {@link Facets} is nullable
   * @param nullable
   * @return {@link Facets} for method chaining
   */
  public Facets setNullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  /**
   * Sets the fixed length of this {@link Facets}
   * @param fixedLength
   * @return {@link Facets} for method chaining
   */
  public Facets setFixedLength(Boolean fixedLength) {
    this.fixedLength = fixedLength;
    return this;
  }

  /**
   * Sets if this {@link Facets} is in Unicode
   * @param unicode
   * @return {@link Facets} for method chaining
   */
  public Facets setUnicode(Boolean unicode) {
    this.unicode = unicode;
    return this;
  }

  /**
   * Sets the default value of this {@link Facets}
   * @param defaultValue
   * @return {@link Facets} for method chaining
   */
  public Facets setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * Sets the maximum length of this {@link Facets}
   * @param maxLength
   * @return {@link Facets} for method chaining
   */
  public Facets setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Sets the precision of this {@link Facets}
   * @param precision
   * @return {@link Facets} for method chaining
   */
  public Facets setPrecision(Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Sets the scale of this {@link Facets}
   * @param scale
   * @return {@link Facets} for method chaining
   */
  public Facets setScale(Integer scale) {
    this.scale = scale;
    return this;
  }

  /**
   * Sets the collation of this {@link Facets}
   * @param collation
   * @return {@link Facets} for method chaining
   */
  public Facets setCollation(String collation) {
    this.collation = collation;
    return this;
  }

  /**
   * Sets the {@link EdmConcurrencyMode} of this {@link Facets}
   * @param concurrencyMode
   * @return {@link Facets} for method chaining
   */
  public Facets setConcurrencyMode(EdmConcurrencyMode concurrencyMode) {
    this.concurrencyMode = concurrencyMode;
    return this;
  }

}