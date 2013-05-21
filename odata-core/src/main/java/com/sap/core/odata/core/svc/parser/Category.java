package com.sap.core.odata.core.svc.parser;

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
  public CommonAttributes getCommonAttribute();

  /**
   * Get the label
   * 
   * @return label as String
   */
  public String getLabel();
}
