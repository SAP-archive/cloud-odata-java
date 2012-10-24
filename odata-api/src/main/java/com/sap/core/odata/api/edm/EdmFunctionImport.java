package com.sap.core.odata.api.edm;

import java.util.Collection;

/**
 * A CSDL FunctionImport element
 * 
 * EdmFunctionImport can be used model functions which have input parameters, an associated HTTP Method
 * and a return type which can be of different kinds:
 * 
 * <li>{@link EdmSimpleType} or a collection of simple types
 * <li>{@link EdmEntityType} or a collection of entity types
 * <li>{@link EdmEntitySet}
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmFunctionImport extends EdmNamed {

  /**
   * Get the parameter by name
   * 
   * @param name
   * @return {@link EdmParameter}
   * @throws EdmException
   */
  EdmParameter getParameter(String name) throws EdmException;

  /**
   * Get all parameter names
   * 
   * @return collection of parameter names of type Collection<String>
   * @throws EdmException
   */
  Collection<String> getParameterNames() throws EdmException;

  /**
   * Get the edm entity set
   * 
   * @return {@link EdmEntitySet}
   * @throws EdmException
   */
  EdmEntitySet getEntitySet() throws EdmException;

  /**
   * Get the HTTP Method
   * 
   * @return HTTP Method as String
   * @throws EdmException
   */
  String getHttpMethod() throws EdmException;

  /**
   * Get the return type
   * 
   * @return {@link EdmTyped}
   * @throws EdmException
   */
  EdmTyped getReturnType() throws EdmException;

  /**
   * Get the entity container the function import is contained in
   * 
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer() throws EdmException;
}