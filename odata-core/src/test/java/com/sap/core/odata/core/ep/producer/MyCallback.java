package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.HashMap;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class MyCallback implements ODataCallback, OnWriteEntryContent, OnWriteFeedContent {

  private AbstractProviderTest dataProvider;
  private URI baseUri;

  public MyCallback(final AbstractProviderTest dataProvider, final URI baseUri) {
    this.dataProvider = dataProvider;
    this.baseUri = baseUri;
  }

  @Override
  public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) {
    WriteFeedCallbackResult result = new WriteFeedCallbackResult();
    try {
      if ("Rooms".equals(context.getSourceEntitySet().getName())) {
        if ("nr_Employees".equals(context.getNavigationProperty().getName())) {
          result.setBaseUri(baseUri);
          result.setFeedData(dataProvider.getEmployeesData());
          HashMap<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
          for (String navPropName : context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
            callbacks.put(navPropName, this);
          }
          result.setCallbacks(callbacks);
        }
      }
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmException", e);
    }
    return result;
  }

  @Override
  public WriteEntryCallbackResult retrieveEntryResult(final WriteEntryCallbackContext context) {
    WriteEntryCallbackResult result = new WriteEntryCallbackResult();
    try {
      if ("Employees".equals(context.getSourceEntitySet().getName())) {
        if ("ne_Room".equals(context.getNavigationProperty().getName())) {
          result.setBaseUri(baseUri);
          result.setEntryData(dataProvider.getRoomData());
          HashMap<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
          for (String navPropName : context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
            callbacks.put(navPropName, this);
          }
          result.setCallbacks(callbacks);
        } else if ("ne_Team".equals(context.getNavigationProperty().getName())) {
          result.setEntryData(null);
        }
      }
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmException:", e);
    }
    return result;
  }

}
