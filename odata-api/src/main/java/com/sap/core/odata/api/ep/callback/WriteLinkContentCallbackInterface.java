package com.sap.core.odata.api.ep.callback;


public interface WriteLinkContentCallbackInterface extends Callback{

  public CallbackResult getLinkContentInformation(String fromEntitySetName, String navigationPropetyName);
  
}
