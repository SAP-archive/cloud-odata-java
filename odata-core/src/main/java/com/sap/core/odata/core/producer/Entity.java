package com.sap.core.odata.core.producer;

public interface Entity {
  ODataResponse read();

  ODataResponse exists();

  ODataResponse update();

  ODataResponse delete();
}
