package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataErrorHandlerCallback;
import com.sap.core.odata.api.processor.ODataResponse;

public class ODataErrorHandlerCallbackImpl implements ODataErrorHandlerCallback {

  @Override
  public ODataResponse handleError(final ODataResponse response, final Exception error) {
    return ODataResponse.entity("bla").status(HttpStatusCodes.BAD_REQUEST).contentHeader("text/html").build();
  }

}
