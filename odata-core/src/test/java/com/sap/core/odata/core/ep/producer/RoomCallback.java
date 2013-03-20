package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.ep.callback.Callback;
import com.sap.core.odata.api.ep.callback.CallbackResult;

public class RoomCallback implements Callback {

  private Map<String, Object> roomData;
  private HashMap<String, Callback> callbacksRoom;
  private URI baseUri;

  public RoomCallback(Map<String, Object> data, URI baseUri) {
    super();
    roomData = data;
    this.baseUri = baseUri;
  }

  @Override
  public CallbackResult retriveResult(Map<String, Object> key) {
    CallbackResult result = new CallbackResult();
    result.setData(roomData);
    result.setCallbacks(callbacksRoom);
    result.setBaseUri(baseUri);

    return result;
  }

  //    @Override
  //    public LinkContentInformation getLinkContentInformation(String fromEntitySetName, String navigationPropetyName) {
  //      try {
  //        if ("Employees".equals(fromEntitySetName) && "ne_Room".equals(navigationPropetyName)) {
  //
  //          LinkContentInformation info = new LinkContentInformation();
  //          info.setToEntitySet(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"));
  //          info.setData(roomData);
  //          return info;
  //        } else {
  //          throw new ODataRuntimeException("Not expected");
  //        }
  //      } catch (EdmException e) {
  //        throw new ODataRuntimeException("Not expected",e);
  //      } catch (ODataException e) {
  //        throw new ODataRuntimeException("Not expected",e);
  //      }
  //
  //    }

}
