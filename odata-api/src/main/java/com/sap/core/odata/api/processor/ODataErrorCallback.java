package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.exception.ODataApplicationException;

/**
 * This interface is called if an error occurred and is process inside the exception mapper.
 * @author SAP AG
 *
 */
public interface ODataErrorCallback extends ODataCallback {
  /**
   * This method can be used to handle an error differently than the exception mapper would. 
   * <br>Any returned Response will be directly transported to the client.
   * <br>Any thrown {@link ODataApplicationException} will be transformed into the OData error format.
   * <br>Any thrown runtime exception will result in an 500 Internal Server error with the Text: "Exception during error handling occurred!" No OData formatting will be applied.
   * <br>To serialize an error into the OData format the {@link com.sap.core.odata.api.ep.EntityProvider} writeErrorDocument can be used.
   * @param context of this error
   * @return the response which will be propagated to the client
   * @throws ODataApplicationException
   */
  ODataResponse handleError(ODataErrorContext context) throws ODataApplicationException;
}
