package com.sap.core.odata.core.producer;

public interface EntityLinks {
  ODataResponse read();

  ODataResponse count();

  ODataResponse createLink();
}
