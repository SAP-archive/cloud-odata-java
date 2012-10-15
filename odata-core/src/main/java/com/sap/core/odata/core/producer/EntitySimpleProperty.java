package com.sap.core.odata.core.producer;

import javax.ws.rs.core.Response;

public interface EntitySimpleProperty {
  Response read();

  Response update();
}
