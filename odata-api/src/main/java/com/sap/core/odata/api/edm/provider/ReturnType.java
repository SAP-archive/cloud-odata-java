package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 * <p>Objects of this Class represent a return type
 */
public class ReturnType {

  private FullQualifiedName qualifiedName;
  private EdmMultiplicity multiplicity;

  /**
   * @return {@link FullQualifiedName} of this {@link ReturnType}
   */
  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  /**
   * @return {@link EdmMultiplicity} of this {@link ReturnType}
   */
  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} of this {@link ReturnType}
   * @param qualifiedName
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setQualifiedName(FullQualifiedName qualifiedName) {
    this.qualifiedName = qualifiedName;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EdmMultiplicity} of this {@link ReturnType}
   * @param multiplicity
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setMultiplicity(EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  @Override
  public String toString() {
    if (EdmMultiplicity.MANY == multiplicity) {
      return "Collection(" + qualifiedName + ")";
    } else {
      return qualifiedName.toString();
    }
  }
  
  
}