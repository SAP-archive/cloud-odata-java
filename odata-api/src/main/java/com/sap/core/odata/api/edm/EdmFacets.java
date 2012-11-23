package com.sap.core.odata.api.edm;

/**
 * <p>A Facet is an element defined in CSDL that provides information
 * that specializes the usage of a type.</p>
 * <p>Do not implement this interface. This interface is intended for usage only.</p>
 * @author SAP AG
 */
public interface EdmFacets {

  /**
   * Get the information if the type in use is nullable
   * 
   * @return <code>true</code> if the type in use is nullable
   */
  Boolean isNullable();

  /**
   * Get the default value of the type in use
   * 
   * @return a default value of the type in use as String
   */
  String getDefaultValue();

  /**
   * Get the maximum length of the type in use
   *
   * @return the maximum length of the type in use as Integer
   */
  Integer getMaxLength();

  /**
   * Get the information if the type in has a fixed length
   * 
   * @return <code>true</code> if the type in use has a fixed length
   */
  Boolean isFixedLength();

  /**
   * Get the precision of the type in use
   * 
   * @return the precision of the type in use as Integer
   */
  Integer getPrecision();

  /**
   * Get the scale of the type in use
   * 
   * @return the scale of the type in use as Integer
   */
  Integer getScale();

  /**
   * Get the information if UNICODE or ASCII characters are used. Default is UNICODE.
   * 
   * @return <code>true</code> if UNICODE characters are used
   */
  Boolean isUnicode();

  /**
   * Get the sorting sequence to be used.
   * 
   * @return the sorting sequence as String
   */
  String getCollation();

  /**
   * Get the information if the value of the type in use should be used for optimistic concurrency checks.
   * 
   * @return {@link EdmConcurrencyMode}
   */
  EdmConcurrencyMode getConcurrencyMode();
}