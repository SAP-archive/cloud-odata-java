package com.sap.core.odata.testutils.fit;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.service.ODataServiceFactory;

public class ServiceFactory implements ODataServiceFactory {

  private static ODataProcessor processor;
  private static EdmProvider edmProvider;

  @Override
  public ODataProcessor createProcessor() throws ODataException {

    if (ServiceFactory.processor == null) {
      throw new NullPointerException();
    }

    return ServiceFactory.processor;
  }

  public static void setProcessor(ODataProcessor processor) {
    ServiceFactory.processor = processor;
  }

  @Override
  public EdmProvider createProvider() throws ODataException {
    if (ServiceFactory.edmProvider == null) {
      throw new NullPointerException();
    }

    return ServiceFactory.edmProvider;
  }

  public static void setProvider(EdmProvider edmProvider) {
    ServiceFactory.edmProvider = edmProvider;
  }

}
