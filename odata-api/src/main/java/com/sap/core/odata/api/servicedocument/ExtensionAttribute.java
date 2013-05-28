package com.sap.core.odata.api.servicedocument;

/**
 * A ExtensionAttribute
 * <p>ExtensionAttribute is an attribute of an extension element
 * @author SAP AG
 */
public interface ExtensionAttribute {
  /**
   * Get the namespace 
   * 
   * @return namespace as String
   */
  public String getNamespace();

  /**
   * Get the prefix of the attribute
   * 
   * @return prefix as String
   */
  public String getPrefix();

  /**
   * Get the local name of the attribute
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
}
