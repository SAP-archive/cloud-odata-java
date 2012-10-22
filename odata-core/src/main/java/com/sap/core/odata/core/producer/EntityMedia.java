package com.sap.core.odata.core.producer;

public interface EntityMedia {
  ODataResponseImpl read();

  ODataResponseImpl update();

  ODataResponseImpl delete();
}
