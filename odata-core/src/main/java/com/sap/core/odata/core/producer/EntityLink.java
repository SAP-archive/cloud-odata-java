package com.sap.core.odata.core.producer;

public interface EntityLink {
  ODataResponse read();

  ODataResponse exists();

  ODataResponse update();

  ODataResponse delete();
}
