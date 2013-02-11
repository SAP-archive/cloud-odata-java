package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AnnotationElement element
 * <p>EdmAnnotationElement is a custom XML element which can be applied to a CSDL element. 
 * @author SAP AG
 */
public interface EdmAnnotationElement {

  /**
   * Get the namespace of the custom element
   * 
   * @return String
   */
  String getNamespace();

  /**
   * Get the prefix of the custom element
   * 
   * @return String
   */
  String getPrefix();

  /**
   * Get the name of the custom element
   * 
   * @return String
   */
  String getName();

  /**
   * Get the XML data of the custom element
   * 
   * @return String
   */
  String getXmlData();
}