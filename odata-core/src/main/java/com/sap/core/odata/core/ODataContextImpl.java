package com.sap.core.odata.core;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  private ODataService service;

  private ODataUriInfo uriInfo;

  public void setService(ODataService service) {
    this.service = service;
  }

  @Override
  public ODataService getService() throws ODataException {
    return this.service;
  }

  public void setUriInfo(ODataUriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  @Override
  public ODataUriInfo getUriInfo() throws ODataException {
    return this.uriInfo;
  }

}
