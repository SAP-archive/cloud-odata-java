package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;

public interface ODataProcessor {

  Edm getEntityDataModel() throws ODataException;
  
  void setContext(ODataContext context) throws ODataException;

  ODataContext getContext() throws ODataException;

}
