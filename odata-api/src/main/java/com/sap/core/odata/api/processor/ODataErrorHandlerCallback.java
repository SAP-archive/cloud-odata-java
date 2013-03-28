package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.ODataCallback;

public interface ODataErrorHandlerCallback extends ODataCallback {

  ODataResponse handleError(ODataResponse response, Exception error);

}
