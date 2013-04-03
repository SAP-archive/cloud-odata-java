package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataErrorCallback;

public class ODataServiceFactoryImpl extends ODataServiceFactory {

  public ODataServiceFactoryImpl() {
    super();
  }

  @Override
  public ODataService createService(final ODataContext ctx) throws ODataException {
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
    T callback = null;

    if (callbackInterface == ODataErrorCallback.class) {
      callback = (T) new ODataErrorHandlerCallbackImpl();
    }

    return callback;
  }

}
