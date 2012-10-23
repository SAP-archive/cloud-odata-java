package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.rest.ODataLocator;
import com.sap.core.odata.api.rest.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.rest.RuntimeDelegate;

public class RuntimeDelegateImpl extends RuntimeDelegate{

  @Override
  public ODataResponseBuilder createODataResponseBuilder() {
    return new ODataResponseBuilderImpl();
  }

  @Override
  public Class<?> getExceptionMapper() {
    return ODataExceptionMapperImpl.class;
  }

  @Override
  public ODataLocator createODataLocator() {
    return new ODataLocatorImpl();
  }

}
