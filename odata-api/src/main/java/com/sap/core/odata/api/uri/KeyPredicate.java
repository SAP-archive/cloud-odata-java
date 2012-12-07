package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmProperty;


/**
 * Key predicate interface
 * @author SAP AG
 */
public interface KeyPredicate {

  /**
   * @return String literal
   */
  public String getLiteral();

  /**
   * @return {@link EdmProperty} property
   */
  public EdmProperty getProperty();

}
