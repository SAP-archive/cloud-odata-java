package com.sap.core.odata.core.producer;

public interface EntityLink {
  ODataResponseImpl read();

  ODataResponseImpl exists();

  ODataResponseImpl update();

  ODataResponseImpl delete();
}
