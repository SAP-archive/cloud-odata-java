package com.sap.core.odata.core.producer;

public interface EntityValueProperties {
  ODataResponseImpl read();

  ODataResponseImpl update();

  ODataResponseImpl delete();
}
