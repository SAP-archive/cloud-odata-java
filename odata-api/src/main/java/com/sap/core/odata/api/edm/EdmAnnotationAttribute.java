/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AnnotationAttribute element. 
 * <p>EdmAnnotationAttribute is a custom XML attribute which can be applied to a CSDL element.
 * @author SAP AG
 */
public interface EdmAnnotationAttribute {

  /**
   * Get the namespace of the custom attribute
   * 
   * @return String
   */
  String getNamespace();

  /**
   * Get the prefix of the custom attribute
   * 
   * @return String
   */
  String getPrefix();

  /**
   * Get the name of the custom attribute
   * 
   * @return String
   */
  String getName();

  /**
   * Get the text of the custom attribute
   * 
   * @return String
   */
  String getText();
}