package com.sap.core.odata.api.servicedocument;

import java.util.List;

/**
 * A ExtensionElement
 * <p>ExtensionElement is an element that is defined in any namespace except 
 * the namespace "app"
 * @author SAP AG
 */
public interface ExtensionElement {
  /**
   * Get the namespace 
   * 
   * @return namespace as String
   */
  public String getNamespace();

  /**
   * Get the prefix of the element
   * 
   * @return prefix as String
   */
  public String getPrefix();

  /**
   * Get the local name of the element
   * 
   * @return name as String
   */
  public String getName();

  /**
   * Get the text
   * 
   * @return text as String
   */
  public String getText();

  /**
   * Get nested elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getElements();

  /**
   * Get attributes
   * 
   * @return a list of {@link ExtensionAttribute}
   */
  public List<ExtensionAttribute> getAttributes();
}
