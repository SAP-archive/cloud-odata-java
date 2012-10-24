package com.sap.core.odata.api.edm;

/**
 * A CSDL AnnotationAttribute element
 * 
 * EdmAnnotationAttribute is a custom XML attribute which can be applied to a CSDL element.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
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