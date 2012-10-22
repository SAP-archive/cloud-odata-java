package com.sap.core.odata.core.producer;

public interface EntityLinks {
  ODataResponseImpl read();

  ODataResponseImpl count();

  ODataResponseImpl createLink();
}
