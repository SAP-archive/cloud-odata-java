package com.sap.core.odata.core.producer;

public interface EntityMedia {
  ODataResponse read();

  ODataResponse update();

  ODataResponse delete();
}
