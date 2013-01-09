package com.sap.core.odata.api;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;

/**
 * Creates instance of custom OData service.
 * 
 * @author SAP AG
 */
public interface ODataServiceFactory {

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
  ODataService createService(ODataContext ctx) throws ODataException;
}