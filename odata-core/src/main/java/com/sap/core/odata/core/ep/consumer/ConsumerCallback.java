package com.sap.core.odata.core.ep.consumer;

public interface ConsumerCallback {

  CallbackResult callback(ConsumerProperties consumerProperties, CallbackInfo callbackInfo);
}
