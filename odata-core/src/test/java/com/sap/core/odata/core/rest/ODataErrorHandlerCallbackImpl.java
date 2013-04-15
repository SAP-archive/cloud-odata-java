/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;

public class ODataErrorHandlerCallbackImpl implements ODataErrorCallback {

  @Override
  public ODataResponse handleError(final ODataErrorContext context) {
    return ODataResponse.entity("bla").status(HttpStatusCodes.BAD_REQUEST).contentHeader("text/html").build();
  }

}
