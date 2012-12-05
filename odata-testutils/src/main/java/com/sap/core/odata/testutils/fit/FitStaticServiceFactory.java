package com.sap.core.odata.testutils.fit;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.api.service.ODataSingleProcessorService;

public class FitStaticServiceFactory implements ODataServiceFactory {

  private static ODataSingleProcessorService service;

  public static void setService(ODataSingleProcessorService service) {
    FitStaticServiceFactory.service = service;
  }

  @Override
  public ODataService createService() throws ODataException {
    if (FitStaticServiceFactory.service == null) {
      throw new NullPointerException("no static service set for JUnit test");
    }

    return FitStaticServiceFactory.service;
  }

}
