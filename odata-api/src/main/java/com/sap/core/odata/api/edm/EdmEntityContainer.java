package com.sap.core.odata.api.edm;

/**
 * A CSDL EntityContainer element
 * 
 * EdmEntityContainer holds the information of EntitySets, FunctionImports and AssociationSets contained
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmEntityContainer extends EdmNamed {

  /**
   * Get contained EntitySet by name
   * 
   * @param name
   * @return {@link EdmEntitySet}
   * @throws EdmException
   */
  EdmEntitySet getEntitySet(String name) throws EdmException;

  /**
   * Get contained FunctionImport by name
   * 
   * @param name
   * @return {@link EdmFunctionImport}
   * @throws EdmException
   */
  EdmFunctionImport getFunctionImport(String name) throws EdmException;

  /**
   * Get contained AssociationSet by providing the source entity set and the navigation property
   * 
   * @param sourceEntitySet of type {@link EdmEntitySet}
   * @param navigationProperty of type {@link EdmNavigationProperty}
   * @return {@link EdmAssociationSet}
   * @throws EdmException
   */
  EdmAssociationSet getAssociationSet(EdmEntitySet sourceEntitySet, EdmNavigationProperty navigationProperty) throws EdmException;
}