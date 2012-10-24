package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmProperty;


/**
 * @author SAP AG
 * Key predicate interface
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
