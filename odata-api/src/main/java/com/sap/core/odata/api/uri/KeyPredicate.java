package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;

/**
 * Key predicate, consisting of a simple-type property and its value as String literal
 * @DoNotImplement
 * @author SAP AG
 */
public interface KeyPredicate {

  /**
   * <p>Gets the literal String in default representation.</p>
   * <p>The description for {@link EdmLiteral} has some motivation for using
   * this representation.</p> 
   * @return String literal in default (<em>not</em> URI) representation
   * @see EdmLiteralKind
   */
  public String getLiteral();

  /**
   * Gets the key property.
   * @return {@link EdmProperty} simple-type property
   */
  public EdmProperty getProperty();

}
