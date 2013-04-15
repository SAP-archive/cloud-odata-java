/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this Class represent a return type of a function import
 * @author SAP AG
 */
public class ReturnType {

  private FullQualifiedName typeName;
  private EdmMultiplicity multiplicity;

  /**
   * @return {@link FullQualifiedName} type of this {@link ReturnType}
   */
  public FullQualifiedName getTypeName() {
    return typeName;
  }

  /**
   * @return {@link EdmMultiplicity} of this {@link ReturnType}
   */
  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * Sets the type  of this {@link ReturnType} via the types {@link FullQualifiedName}
   * @param qualifiedName
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setTypeName(final FullQualifiedName qualifiedName) {
    typeName = qualifiedName;
    return this;
  }

  /**
   * Sets the {@link EdmMultiplicity} of this {@link ReturnType}
   * @param multiplicity
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setMultiplicity(final EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  @Override
  public String toString() {
    if (EdmMultiplicity.MANY == multiplicity) {
      return "Collection(" + typeName + ")";
    } else {
      return typeName.toString();
    }
  }

}