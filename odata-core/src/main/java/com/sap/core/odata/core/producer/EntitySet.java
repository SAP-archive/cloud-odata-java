package com.sap.core.odata.core.producer;

public interface EntitySet {
  ODataResponse read();

  ODataResponse count();

  ODataResponse createEntity();
}
