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
  
  public ODataService createODataSingleProcessorService(EdmProvider provider, ODataSingleProcessor processor) {
    return RuntimeDelegate.createODataSingleProcessorService(provider, processor);
  }

}