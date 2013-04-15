/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * Customizable Feed property mappings for the AtomPub Format as defined in the OData specification.
 * @author SAP AG
 */
public interface EdmCustomizableFeedMappings {

  /**
   * Get the information if the property should be kept in the content
   * 
   * @return <code>true</code> if the property must be kept in the content
   */
  public Boolean isFcKeepInContent();

  /**
   * Get the content kind
   * 
   * @return {@link EdmContentKind}
   */
  public EdmContentKind getFcContentKind();

  /**
   * Get the XML namespace prefix
   * 
   * @return String
   */
  public String getFcNsPrefix();

  /**
   * Get the XML namespace URI
   * 
   * @return String
   */
  public String getFcNsUri();

  /**
   * Get the source path
   * 
   * @return String
   */
  public String getFcSourcePath();

  /**
   * Get the target path
   * 
   * @return String
   */
  public String getFcTargetPath();
}
