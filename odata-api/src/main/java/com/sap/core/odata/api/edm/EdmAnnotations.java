/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

import java.util.List;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmAnnotations holds all annotation attributes and elements for a specific CSDL element.
 * @author SAP AG
 */
public interface EdmAnnotations {

  /**
   * Get all annotation elements for the CSDL element
   * 
   * @return List of {@link EdmAnnotationElement}
   */
  List<? extends EdmAnnotationElement> getAnnotationElements();

  /**
   * Get annotation element by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */

  EdmAnnotationElement getAnnotationElement(String name, String namespace);

  /**
   * Get all annotation attributes for the CSDL element
   * 
   * @return List of {@link EdmAnnotationAttribute}
   */
  List<? extends EdmAnnotationAttribute> getAnnotationAttributes();

  /**
   * Get annotation attribute by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */
  EdmAnnotationAttribute getAnnotationAttribute(String name, String namespace);
}