package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

import com.sap.core.odata.core.edm.Edm;

public interface Metadata {

  Edm getEdm();
  
  Response read();
  
}
