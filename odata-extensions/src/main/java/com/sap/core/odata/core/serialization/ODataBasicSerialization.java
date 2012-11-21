package com.sap.core.odata.core.serialization;

import com.sap.core.odata.api.annotations.ODataDeSerializer;
import com.sap.core.odata.api.annotations.ODataSerializer;

public class ODataBasicSerialization implements ODataSerializer<Object>, ODataDeSerializer<Object> {

  @Override
  public Object deserialize(Object input) {
    return input;
  }
  
  @Override
  public Object serialize(Object obj) {
    return obj;
  }

}
