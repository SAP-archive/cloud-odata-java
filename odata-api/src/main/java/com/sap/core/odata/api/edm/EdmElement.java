package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmElement is the base interface for {@link EdmParameter} and {@link EdmProperty} and provides
 * the information by which facets further specialize the usage of the type.
 * @author SAP AG
 */
public interface EdmElement extends EdmMappable, EdmTyped {

  /**
   * Get the facet information for an element
   * 
   * @return {@link EdmFacets}
   * @throws EdmException
   */
  EdmFacets getFacets() throws EdmException;
}