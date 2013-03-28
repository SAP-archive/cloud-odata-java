package com.sap.core.odata.api;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * Creates instance of custom OData service.
 * 
 * @author SAP AG
 */
public abstract class ODataServiceFactory {

  /**
   * Label used in web.xml to assign servlet init parameter to factory class instance.
   */
  public static final String FACTORY_LABEL = "com.sap.core.odata.service.factory";

  /**
   * Label used in web.xml to assign servlet init parameter for a path split (service resolution).
   */
  public static final String PATH_SPLIT_LABEL = "com.sap.core.odata.path.split";

  /**
   * Create instance of custom {@link ODataService}.
   * @param ctx OData context object
   * @return A new service instance.
   * @throws ODataException in case of error
   */
  public abstract ODataService createService(ODataContext ctx) throws ODataException;

  /**
   * Create a default service instance based on </code>ODataSingleProcessor<code>.
   * @param provider A custom <code>EdmProvider</code> implementation.
   * @param processor A custom processor implementation derived from <code>ODataSingleProcessor</code> .
   * @return A new default <code>ODataSingleProcessorService</code> instance.
   */
  public ODataService createODataSingleProcessorService(final EdmProvider provider, final ODataSingleProcessor processor) {
    return RuntimeDelegate.createODataSingleProcessorService(provider, processor);
  }

  /**
   * A service can return implementation classes for various callback interfaces.
   * @param callbackInterface a interface type to query for implementation
   * @return a callback implementation for this interface or null
   */
  public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
    return null;
  }

}