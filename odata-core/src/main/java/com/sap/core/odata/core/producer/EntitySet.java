package com.sap.core.odata.core.producer;

public interface EntitySet {
  ODataResponseImpl read();

  ODataResponseImpl count();

  ODataResponseImpl createEntity();
}
