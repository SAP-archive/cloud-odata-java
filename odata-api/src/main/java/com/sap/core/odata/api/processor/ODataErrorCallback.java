package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.ODataCallback;

public interface ODataErrorCallback extends ODataCallback {

  ODataResponse handleError(ODataErrorContext context);
  
//  TODO: CA claryfiy if Application Exception makes sense or if it has to be Exception
//  ODataResponse handleError(ODataErrorCallbackContext context)throws ODataApplicationException;
  
}
