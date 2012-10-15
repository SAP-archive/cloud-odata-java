package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

public interface EntitySet {
  Response read();
  Response count();
  Response cretateEntity();
}
