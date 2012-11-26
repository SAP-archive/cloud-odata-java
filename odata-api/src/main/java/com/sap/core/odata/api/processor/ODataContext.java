package com.sap.core.odata.api.processor;


import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataService;

/**
 * Compilation of generic context objects. 
 */
public interface ODataContext {

  /**
   * Access to untyped context objects. 
   * @param key access object by key
   * @return an object or null if not found
   * @throws {@link ODataException}
   */
  <T> T getObject(String key) throws ODataException;

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