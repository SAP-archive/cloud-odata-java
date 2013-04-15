/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmAnnotatable can be applied to CSDL elements as described in the Conceptual Schema Definition Language.
 * @author SAP AG
 */
public interface EdmAnnotatable {

  /**
   * Get all annotations applied to an EDM element
   * 
   * @return {@link EdmAnnotations}
   * @throws EdmException
   */
  EdmAnnotations getAnnotations() throws EdmException;
}