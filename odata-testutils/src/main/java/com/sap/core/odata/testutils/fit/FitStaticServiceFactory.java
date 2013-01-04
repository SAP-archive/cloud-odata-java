package com.sap.core.odata.testutils.fit;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;

public class FitStaticServiceFactory implements ODataServiceFactory {

  private static ODataSingleProcessorService service;

  public static void setService(ODataSingleProcessorService service) {
    FitStaticServiceFactory.service = service;
  }

  @Override
  public ODataService createService(ODataContext ctx) throws ODataException {
    if (FitStaticServiceFactory.service == null) {
      throw new IllegalArgumentException("no static service set for JUnit test");
    }

    return FitStaticServiceFactory.service;
  }

}
