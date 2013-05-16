package com.sap.core.odata.api.doc;

import java.util.List;

/**
 * A CommonAttributes
 * @author SAP AG
 */
public interface CommonAttributes {
  /**
   * Get the a base URI 
   * 
   * @return base as String
   */
  public String getBase();

  /**
   * Get the natural language for the element
   * 
   * @return language as String
   */
  public String getLang();

  /**
   * Get the list of any attributes
   * 
   * @return list of {@link ExtensionAttribute}
   */
  public List<ExtensionAttribute> getAttributes();

}
