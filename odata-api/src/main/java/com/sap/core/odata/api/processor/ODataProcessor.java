package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

public interface ODataProcessor {

  void setContext(ODataContext context) throws ODataException;

  ODataContext getContext() throws ODataException;

}
