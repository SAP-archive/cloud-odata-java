package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

public interface EntityLink {
  Response read();
  Response exists();
  Response update();
  Response delete();
}
