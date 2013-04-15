/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL EntityContainer element
 * 
 * <p>EdmEntityContainer hold the information of EntitySets, FunctionImports and AssociationSets contained
 * @author SAP AG
 */
public interface EdmEntityContainer extends EdmNamed {

  /**
   * @return <b>boolean</b> true if this is the default container
   */
  boolean isDefaultEntityContainer();

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