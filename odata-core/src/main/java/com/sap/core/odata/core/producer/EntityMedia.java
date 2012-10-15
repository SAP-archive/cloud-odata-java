package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

public interface EntityMedia {
  Response read();

  Response update();

  Response delete();
}
