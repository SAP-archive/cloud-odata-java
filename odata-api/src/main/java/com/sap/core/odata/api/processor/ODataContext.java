package com.sap.core.odata.api.processor;

import java.util.List;

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
   * Returns preceding path segments  
   * @return list of path segments
   */
  List<ODataPathSegment> getPrecedingPathSegmentList();

  /**
   * Returns OData path segments as immutable list  
   * @return list of path segments
   */
  List<ODataPathSegment> getODataPathSegmentList();

  /**
   * @return absolute base uri of the request
   */
  String getBaseUri();

}