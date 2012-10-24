package com.sap.core.odata.api.edm;

import java.util.Collection;

/**
 * EdmAnnotations holds all annotation attributes and elements for a specific CSDL element.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmAnnotations {

  /**
   * Get all annotation elements for the CSDL element
   * 
   * @return Collection of {@link EdmAnnotationElement}
   */
  Collection<EdmAnnotationElement> getAnnotationElements();

  /**
   * Get annotation element by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */
  String getAnnotationElement(String name, String namespace);

  //TODO return type to be discussed, see EdmAnnotationElement

  /**
   * Get all annotation attributes for the CSDL element
   * 
   * @return Collection of {@link EdmAnnotationAttribute}
   */
  Collection<EdmAnnotationAttribute> getAnnotationAttributes();

  /**
   * Get annotation attribute by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */
  String getAnnotationAttribute(String name, String namespace);

  //TODO do we need a generic data container like
  //
  //  Object getData(String name);
}