package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;

/**
 * <p>Key predicate, consisting of a simple-type property and its value as string literal</p>
 * <p><strong>IMPORTANT</strong>:
 * Do not implement this interface. This interface is intended for usage only.</p>
 * @author SAP AG
 */
public interface KeyPredicate {

  /**
   * @return String literal in default (<em>not</em> URI) representation
   * @see EdmLiteralKind
   */
  public String getLiteral();

  /**
   * @return {@link EdmProperty} simple-type property
   */
  public EdmProperty getProperty();

}
