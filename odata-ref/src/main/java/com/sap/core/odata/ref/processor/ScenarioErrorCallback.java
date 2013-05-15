package com.sap.core.odata.ref.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Callback for handling errors by logging internal server errors additionally.
 * @author SAP AG
 */
public class ScenarioErrorCallback implements ODataErrorCallback {

  private static final Logger LOG = LoggerFactory.getLogger(ScenarioErrorCallback.class);

  @Override
  public ODataResponse handleError(final ODataErrorContext context) throws ODataApplicationException {
    if (context.getHttpStatus() == HttpStatusCodes.INTERNAL_SERVER_ERROR) {
      LOG.error("Internal Server Error", context.getException());
    }

    return EntityProvider.writeErrorDocument(context);
  }

}
