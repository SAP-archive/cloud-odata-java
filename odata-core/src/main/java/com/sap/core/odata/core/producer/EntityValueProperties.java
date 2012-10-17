package com.sap.core.odata.core.producer;

public interface EntityValueProperties {
  ODataResponse read();

  ODataResponse update();

  ODataResponse delete();
}
