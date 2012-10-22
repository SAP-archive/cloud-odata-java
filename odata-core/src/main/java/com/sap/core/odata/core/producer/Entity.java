package com.sap.core.odata.core.producer;

public interface Entity {
  ODataResponseImpl read();

  ODataResponseImpl exists();

  ODataResponseImpl update();

  ODataResponseImpl delete();
}
