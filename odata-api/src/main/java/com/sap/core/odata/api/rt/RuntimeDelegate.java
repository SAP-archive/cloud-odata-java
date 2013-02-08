package com.sap.core.odata.api.rt;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.UriParser;

/**
 * Provides access to core implementation classes for interfaces. This class is used 
 * by internal abstract API implementations and it is not intended to be used by others. 
 * 
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public abstract class RuntimeDelegate {

  private static final String IMPLEMENTATION = "com.sap.core.odata.core.rt.RuntimeDelegateImpl";

  /**
   * Create a runtime delegate instance from the core library. The core 
   * library (com.sap.core.odata.core.jar) needs to be included into the classpath
   * of the using application.
   * @return an implementation object
   */
  private static RuntimeDelegateInstance getInstance() {
    RuntimeDelegateInstance delegate;

    try {
      final Class<?> clazz = Class.forName(RuntimeDelegate.IMPLEMENTATION);

      /*
       * We explicitly do not use the singleton pattern to keep the server state free
       * and avoid class loading issues also during hot deployment. 
       */
      final Object object = clazz.newInstance();
      delegate = (RuntimeDelegateInstance) object;

    } catch (final Exception e) {
      throw new RuntimeDelegateException(e);
    }
    return delegate;
  }

  /**
   * An implementation is available in the core library.
   * @com.sap.core.odata.DoNotImplement
   */
  public static abstract class RuntimeDelegateInstance {

    /**
     * Returns a builder for creating response objects with variable parameter sets.
     * @return an implementation object
     */
    protected abstract ODataResponseBuilder createODataResponseBuilder();

    /**
     * Returns a simple type object for given type kind.
     * @param edmSimpleTypeKind 
     * @return an implementation object
     */
    protected abstract EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleTypeKind);

    /**
     * Returns an parser which can parse OData uris based on metadata.
     * @param edm metadata of the implemented service
     * @return an implementation object
     */
    protected abstract UriParser getUriParser(Edm edm);

    /**
     * Returns an implementation of the EDM simple-type facade.
     * @return an implementation object
     */
    protected abstract EdmSimpleTypeFacade getSimpleTypeFacade();

    /**
     * Creates and returns an entity data model.
     * @param provider a provider implemented by the OData service
     * @return an implementation object
     */
    protected abstract Edm createEdm(EdmProvider provider);

    /**
     * Creates and returns a HTTP entity provider. 
     * @return an implementation object
     */
    protected abstract EntityProviderInterface createEntityProvider();

    /**
     * Creates and returns a single processor service. 
     * @param provider a provider implementation for the metadata of the OData service
     * @param processor a single data processor implementation of the OData service
     * @return a implementation object
     */
    protected abstract ODataService createODataSingleProcessorService(EdmProvider provider, ODataSingleProcessor processor);
  }

  /**
   * @see RuntimeDelegateInstance#getEdmSimpleType
   */
  public static EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    return RuntimeDelegate.getInstance().getEdmSimpleType(edmSimpleType);
  }

  /**
   * @see RuntimeDelegateInstance#getSimpleTypeFacade
   */
  public static EdmSimpleTypeFacade getSimpleTypeFacade() {
    return RuntimeDelegate.getInstance().getSimpleTypeFacade();
  }

  /**
   * @see RuntimeDelegateInstance#createODataResponseBuilder
   */
  public static ODataResponseBuilder createODataResponseBuilder() {
    return RuntimeDelegate.getInstance().createODataResponseBuilder();
  }

  /**
   * @see RuntimeDelegateInstance#createEdm
   */
  public static Edm createEdm(EdmProvider provider) {
    return RuntimeDelegate.getInstance().createEdm(provider);
  }

  /**
   * @see RuntimeDelegateInstance#getUriParser
   */
  public static UriParser getUriParser(Edm edm) {
    return RuntimeDelegate.getInstance().getUriParser(edm);
  }

  /**
   * @see RuntimeDelegateInstance#createEntityProvider
   */
  public static EntityProviderInterface createEntityProvider() {
    return RuntimeDelegate.getInstance().createEntityProvider();
  }

  /**
   * @see RuntimeDelegateInstance#createODataSingleProcessorService
   */
  public static ODataService createODataSingleProcessorService(EdmProvider provider, ODataSingleProcessor processor) {
    return RuntimeDelegate.getInstance().createODataSingleProcessorService(provider, processor);
  }

  /**
   * Is thrown in the runtime delegate implementation. Usually a serious class loading problem.
   */
  private static class RuntimeDelegateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RuntimeDelegateException(Exception e) {
      super(e);
    }
  }

}
