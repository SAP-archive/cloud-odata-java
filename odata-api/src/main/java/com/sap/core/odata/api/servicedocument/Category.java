package com.sap.core.odata.api.servicedocument;

/**
 * A Category element
 * <p>Category element 
 * @author SAP AG
 */
public interface Category {
  /**
   * Get the scheme
   * 
   * @return scheme as String
   */
  public String getScheme();

  /**
   * Get the term
   * 
   * @return term as String
   */
  public String getTerm();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();

  /**
   * Get the label
   * 
   * @return label as String
   */
  public String getLabel();
}
