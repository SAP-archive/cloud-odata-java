package com.sap.core.odata.api.rt;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.UriParser;

/**
 * Abstract class to get odata core implementations of api interfaces
 * @author SAP AG
 */
public abstract class RuntimeDelegate {

  private static final String IMPLEMENTATION = "com.sap.core.odata.core.rt.RuntimeDelegateImpl";

  /**
   * Get a RuntimeDelegate Instance through reflection
   * @return {@link RuntimeDelegate} object
   */
  public static RuntimeDelegate getInstance() {
    RuntimeDelegate delegate;

    try {
      Class<?> clazz = Class.forName(RuntimeDelegate.IMPLEMENTATION);

      Object object = clazz.newInstance();
      delegate = (RuntimeDelegate) object;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return delegate;
  }

  /**
   * Get a OData response builder
   * @return {@link ODataResponseBuilder} object
   */
  public abstract ODataResponseBuilder createODataResponseBuilder();

  /**
   * Gets an {@link EdmSimpleType} implementation for a given {@link EdmSimpleTypeKind}
   * @param edmSimpleTypeKind
   * @return {@link EdmSimpleType}
   */
  public abstract EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleTypeKind);

  /**
   * Get a UriParser object
   * @return {@link UriParser} object
   */
  public abstract UriParser getUriParser(Edm edm);

  /**
   * Gets an {@link EdmSimpleType} implementation for a given simple-type name.
   * @param edmSimpleType  name of the simple type
   * @return {@link EdmSimpleType}
   */
  public abstract EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType);

  /**
   * Gets an implementation of the EDM simple-type facade.
   * @return {@link EdmSimpleTypeFacade}
   */
  public abstract EdmSimpleTypeFacade getSimpleTypeFacade();

  /**
   * Creates an entity data model.
   * @param provider A {@link EdmProvider} instance
   * @return {@link Edm} implementation object
   */
  public abstract Edm createEdm(EdmProvider provider);

}
