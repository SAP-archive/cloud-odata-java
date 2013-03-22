package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.HashMap;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.callback.Callback;
import com.sap.core.odata.api.ep.callback.CallbackContext;
import com.sap.core.odata.api.ep.callback.CallbackResult;
import com.sap.core.odata.api.ep.callback.EntryCallbackResult;
import com.sap.core.odata.api.ep.callback.FeedCallbackResult;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class MyCallback implements Callback {

  private AbstractProviderTest dataProvider;
  private URI baseUri;

  public MyCallback(AbstractProviderTest dataProvider, URI baseUri) {
    this.dataProvider = dataProvider;
    this.baseUri = baseUri;
  }

  @Override
  public <T extends CallbackResult> T retriveResult(CallbackContext context, Class<T> callbackResultClass) {
    CallbackResult result;

    try {
      if (callbackResultClass.equals(FeedCallbackResult.class)) {
        FeedCallbackResult internalResult = new FeedCallbackResult();
        if ("Rooms".equals(context.getEntitySet().getName())) {
          if("nr_Employees".equals(context.getNavigationProperty().getName())){
            internalResult.setBaseUri(baseUri);
            internalResult.setFeedData(dataProvider.getEmployeesData());
            HashMap<String, Callback> callbacks = new HashMap<String, Callback>();
            for (String navPropName : context.getEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
              callbacks.put(navPropName, this);
            }
            internalResult.setCallbacks(callbacks);
          }
        }
        result = internalResult;
      } else if (callbackResultClass.equals(EntryCallbackResult.class)) {
        EntryCallbackResult internalResult = new EntryCallbackResult();
        if ("Employees".equals(context.getEntitySet().getName())) {
          if("ne_Room".equals(context.getNavigationProperty().getName())){
            internalResult.setBaseUri(baseUri);
            internalResult.setEntryData(dataProvider.getRoomData());
            HashMap<String, Callback> callbacks = new HashMap<String, Callback>();
            for (String navPropName : context.getEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
              callbacks.put(navPropName, this);
            }
            internalResult.setCallbacks(callbacks);
          } else if("ne_Team".equals(context.getNavigationProperty().getName())){
            internalResult.setEntryData(null);
          }
        }
        result = internalResult;
      } else {
        throw new ODataRuntimeException("Unsupported CallbackResult class");
      }

      return callbackResultClass.cast(result);
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmError", e);
    }
  }

}
