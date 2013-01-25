package com.sap.core.odata.testutil.fit;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;

public class FitStaticServiceFactory extends ODataServiceFactory {

  private static ODataService service;

  public static void setService(ODataService service) {
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
