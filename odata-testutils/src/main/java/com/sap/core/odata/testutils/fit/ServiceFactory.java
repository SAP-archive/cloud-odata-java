package com.sap.core.odata.testutils.fit;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;

public class ServiceFactory implements ODataServiceFactory {

  private static ODataService service;

  public static void setService(ODataService service) {
    ServiceFactory.service = service;
  }

  @Override
  public ODataService createService() throws ODataException {
    if (ServiceFactory.service == null) {
      throw new NullPointerException();
    }

    return ServiceFactory.service;
  }

}
