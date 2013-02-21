package com.sap.core.odata.api.edm;

import java.util.List;

import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;

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
  String getText();
  
  /**
   * Get the child elements of the custom element
   * 
   * @return child elements of this {@link EdmAnnotationElement}
   */
  List<AnnotationElement> getChildElements();
  
  
  /**
   * Get the attributes of this custom element
   * 
   * @return the attributes of this {@link EdmAnnotationElement}
   */
  List<AnnotationAttribute> getAttributes();
  
}