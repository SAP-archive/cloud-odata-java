package com.sap.core.odata.ref.processor;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * 
 * @author SAP AG
 */
public class ScenarioErrorCallback implements ODataErrorCallback {

  private static final Logger LOG = LoggerFactory.getLogger(ScenarioErrorCallback.class);

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.processor.ODataErrorCallback#handleError(com.sap.core.odata.api.processor.ODataErrorContext)
   */
  @Override
  public ODataResponse handleError(final ODataErrorContext context) throws ODataApplicationException {
    try {
      if (context.getHttpStatus() == HttpStatusCodes.INTERNAL_SERVER_ERROR) {
        LOG.error("Internal Server Error", context.getException());
      }

      String contentType = context.getContentType();
      HttpStatusCodes status = context.getHttpStatus();
      String errorCode = context.getErrorCode();
      String message = context.getMessage();
      Locale locale = context.getLocale();
      String innerError = null;

      return EntityProvider.writeErrorDocument(contentType, status, errorCode, message, locale, innerError);
    } catch (EntityProviderException e) {
      throw new ODataApplicationException(context.getMessage(), context.getLocale(), e);
    }
  }

}
