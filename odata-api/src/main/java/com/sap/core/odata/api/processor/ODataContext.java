package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

/**
 * Compilation of generic context objects. 
 */
public interface ODataContext {
  
  <T> T getContextObject(Class<T> clazz) throws ODataException;

}