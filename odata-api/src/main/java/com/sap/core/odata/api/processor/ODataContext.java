package com.sap.core.odata.api.processor;


import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataService;

/**
 * Compilation of generic context objects. 
 */
public interface ODataContext {

  /**
   * @return ODataService related for this context
   * @throws ODataException
   */
  ODataService getService() throws ODataException;

  /**
   * @return a OData URI info object
   * @throws ODataException
   */
  ODataUriInfo getUriInfo() throws ODataException;
  
}