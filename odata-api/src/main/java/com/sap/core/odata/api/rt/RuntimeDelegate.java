package com.sap.core.odata.api.rt;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.FilterParser;
import com.sap.core.odata.api.uri.expression.OrderByParser;

/**
 * Abstract class to get OData core implementations of API interfaces
 * @author SAP AG
 */
public abstract class RuntimeDelegate {

  private static final String IMPLEMENTATION = "com.sap.core.odata.core.rt.RuntimeDelegateImpl";

  /**
   * Get a RuntimeDelegate Instance through reflection
   * @return {@link RuntimeDelegate} object
   */
  private static RuntimeDelegateInstance getInstance() {
    RuntimeDelegateInstance delegate;

    try {
      Class<?> clazz = Class.forName(RuntimeDelegate.IMPLEMENTATION);

      Object object = clazz.newInstance();
      delegate = (RuntimeDelegateInstance) object;

    } catch (Exception e) {
      throw new RuntimeDelegateException(e);
    }
    return delegate;
  }
  
  public static abstract class RuntimeDelegateInstance {
    /**
     * Get a OData response builder
     * @return {@link ODataResponseBuilder} object
     */
    protected abstract ODataResponseBuilder createODataResponseBuilder();
    
    /**
     * Gets an {@link EdmSimpleType} implementation for a given {@link EdmSimpleTypeKind}
     * @param edmSimpleTypeKind
     * @return {@link EdmSimpleType}
     */
    protected abstract EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleTypeKind);
    
    /**
     * Get a UriParser object
     * @return {@link UriParser} object
     */
    protected abstract UriParser getUriParser(Edm edm);
    
    /**
     * Gets an {@link EdmSimpleType} implementation for a given simple-type name.
     * @param edmSimpleType  name of the simple type
     * @return {@link EdmSimpleType}
     */
    protected abstract EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType);
    
    /**
     * Gets an implementation of the EDM simple-type facade.
     * @return {@link EdmSimpleTypeFacade}
     */
    protected abstract EdmSimpleTypeFacade getSimpleTypeFacade();
    
    /**
     * Creates an entity data model.
     * @param provider A {@link EdmProvider} instance
     * @return {@link Edm} implementation object
     */
    protected abstract Edm createEdm(EdmProvider provider);
    
    protected abstract FilterParser getFilterParser(Edm edm, EdmEntityType edmType);
    protected abstract OrderByParser getOrderByParser(Edm edm, EdmEntityType edmType);
    
    /**
     * @param contentType requested content type
     * @return a OData entity provider for requested content type
     * @throws ODataEntityProviderException 
     */
    protected abstract ODataEntityProvider createSerializer(String contentType) throws ODataEntityProviderException;
  }


  public static ODataEntityProvider createSerializer(String contentType) throws ODataEntityProviderException {
    return RuntimeDelegate.getInstance().createSerializer(contentType);
  }

  public static EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    return RuntimeDelegate.getInstance().getEdmSimpleType(edmSimpleType);
  }

  public static EdmSimpleTypeFacade getSimpleTypeFacade() {
    return RuntimeDelegate.getInstance().getSimpleTypeFacade();
  }

  public static ODataResponseBuilder createODataResponseBuilder() {
    return RuntimeDelegate.getInstance().createODataResponseBuilder();
  }

  public static Edm createEdm(EdmProvider provider) {
    return RuntimeDelegate.getInstance().createEdm(provider);
  }

  public static UriParser getUriParser(Edm edm) {
    return RuntimeDelegate.getInstance().getUriParser(edm);
  }

  public static EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType) {
    return RuntimeDelegate.getInstance().getInternalEdmSimpleTypeByString(edmSimpleType);
  }

  public static FilterParser getFilterParser(Edm edm, EdmEntityType edmType) {
    return RuntimeDelegate.getInstance().getFilterParser(edm, edmType);
  }
  
  public static OrderByParser getOrderByParser(Edm edm, EdmEntityType edmType) {
    return RuntimeDelegate.getInstance().getOrderByParser(edm, edmType);
  }
  
  private static class RuntimeDelegateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RuntimeDelegateException(Exception e) {
      super(e);
    }
  }
  
}
