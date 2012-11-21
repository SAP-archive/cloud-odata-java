package com.sap.core.odata.core.serialization;

import java.io.InputStream;

import com.sap.core.odata.api.annotations.ODataDeSerializer;
import com.sap.core.odata.api.annotations.ODataSerializer;

public class ODataStringSerialization implements ODataSerializer<String>, ODataDeSerializer<String> {

  @Override
  public Object deserialize(String input) {
    return input;
  }
  
  public Object deserializeStream(InputStream is) {
    try {
      int available = is.available();
      byte[] buffer = new byte[available];
      is.read(buffer);
      return new String(buffer);
    } catch (Exception e) {
      return "Exception: " + e.getMessage();
    }
  }

  @Override
  public String serialize(Object obj) {
    
    return obj.toString();
  }

}
