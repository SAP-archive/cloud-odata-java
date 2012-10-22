package com.sap.core.odata.core.producer;

import com.sap.core.odata.core.edm.Edm;

public interface Metadata {

  Edm getEdm();

  ODataResponseImpl read();

}
