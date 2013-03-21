package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ep.callback.Callback;
import com.sap.core.odata.api.ep.callback.FeedCallbackResult;

public class EmployeesCallback implements Callback {

  List<Map<String, Object>> employeesData;
  private URI baseUri;
  private HashMap<String, Callback> callbacksEmployee;

  public EmployeesCallback(final List<Map<String, Object>> employeesData, final URI baseUri, final Map<String, Object> roomData) {
    this.employeesData = employeesData;
    this.baseUri = baseUri;

    callbacksEmployee = new HashMap<String, Callback>();
    callbacksEmployee.put("ne_Room", new RoomCallback(roomData, baseUri));

  }

  @Override
  public FeedCallbackResult retriveResult(final Map<String, Object> key) {
    FeedCallbackResult result = new FeedCallbackResult();
    result.setFeedData(employeesData);
    result.setCallbacks(callbacksEmployee);
    result.setBaseUri(baseUri);

    return result;
  }

}
