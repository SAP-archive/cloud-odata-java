package com.sap.core.odata.api.edm;

/**
 * A CSDL AnnotationElement element
 * 
 * EdmAnnotationElement is a custom XML element which can be applied to a CSDL element.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
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
  //TODO return type to be discussed
  String getXmlData();
}