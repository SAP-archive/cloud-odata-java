package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntityLink {
  ODataResponse readEntityLink() throws ODataError;

  ODataResponse existsEntityLink() throws ODataError;

  ODataResponse updateEntityLink() throws ODataError;

  ODataResponse deleteEntityLink() throws ODataError;
}
