/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmFacets;

/**
 * Objects of this class represent the facets an entity type, property or function import can have
 * @author SAP AG
 */
public class Facets implements EdmFacets {

  /** Specification default is TRUE but we won't set it here because 
   * we want to know if it's set explicitly by an application. */
  Boolean nullable;
  String defaultValue;
  Integer maxLength;
  Boolean fixedLength;
  Integer precision;
  Integer scale;
  Boolean unicode;
  String collation;
  EdmConcurrencyMode concurrencyMode;

  @Override
  public Boolean isNullable() {
    return nullable;
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public Integer getMaxLength() {
    return maxLength;
  }

  @Override
  public Boolean isFixedLength() {
    return fixedLength;
  }

  @Override
  public Integer getPrecision() {
    return precision;
  }

  @Override
  public Integer getScale() {
    return scale;
  }

  @Override
  public Boolean isUnicode() {
    return unicode;
  }

  @Override
  public String getCollation() {
    return collation;
  }

  @Override
  public EdmConcurrencyMode getConcurrencyMode() {
    return concurrencyMode;
  }

  /**
   * Sets if this {@link Facets} is nullable
   * @param nullable
   * @return {@link Facets} for method chaining
   */
  public Facets setNullable(final Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  /**
   * Sets the fixed length of this {@link Facets}
   * @param fixedLength
   * @return {@link Facets} for method chaining
   */
  public Facets setFixedLength(final Boolean fixedLength) {
    this.fixedLength = fixedLength;
    return this;
  }

  /**
   * Sets if this {@link Facets} is in Unicode
   * @param unicode
   * @return {@link Facets} for method chaining
   */
  public Facets setUnicode(final Boolean unicode) {
    this.unicode = unicode;
    return this;
  }

  /**
   * Sets the default value of this {@link Facets}
   * @param defaultValue
   * @return {@link Facets} for method chaining
   */
  public Facets setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * Sets the maximum length of this {@link Facets}
   * @param maxLength
   * @return {@link Facets} for method chaining
   */
  public Facets setMaxLength(final Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Sets the precision of this {@link Facets}
   * @param precision
   * @return {@link Facets} for method chaining
   */
  public Facets setPrecision(final Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Sets the scale of this {@link Facets}
   * @param scale
   * @return {@link Facets} for method chaining
   */
  public Facets setScale(final Integer scale) {
    this.scale = scale;
    return this;
  }

  /**
   * Sets the collation of this {@link Facets}
   * @param collation
   * @return {@link Facets} for method chaining
   */
  public Facets setCollation(final String collation) {
    this.collation = collation;
    return this;
  }

  /**
   * Sets the {@link EdmConcurrencyMode} of this {@link Facets}
   * @param concurrencyMode
   * @return {@link Facets} for method chaining
   */
  public Facets setConcurrencyMode(final EdmConcurrencyMode concurrencyMode) {
    this.concurrencyMode = concurrencyMode;
    return this;
  }

  @Override
  public String toString() {
    final List<String> values = new ArrayList<String>();
    if (nullable != null) {
      values.add("Nullable=" + nullable);
    }
    if (defaultValue != null) {
      values.add("DefaultValue=" + defaultValue);
    }
    if (maxLength != null) {
      values.add("MaxLength=" + maxLength);
    }
    if (fixedLength != null) {
      values.add("FixedLength=" + fixedLength);
    }
    if (precision != null) {
      values.add("Precision=" + precision);
    }
    if (scale != null) {
      values.add("Scale=" + scale);
    }
    if (unicode != null) {
      values.add("Unicode=" + unicode);
    }
    if (collation != null) {
      values.add("Collation=" + collation);
    }
    if (concurrencyMode != null) {
      values.add("ConcurrencyMode=" + concurrencyMode);
    }
    return values.toString();
  }
}
