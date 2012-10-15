package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

public interface EntityLinks {
  Response read();

  Response count();

  Response createLink();
}
