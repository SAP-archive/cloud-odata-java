package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;

public class ReturnType {

  private FullQualifiedName qualifiedName;
  private EdmMultiplicity multiplicity;

  public ReturnType(FullQualifiedName qualifiedName, EdmMultiplicity multiplicity) {
    this.qualifiedName = qualifiedName;
    this.multiplicity = multiplicity;
  }

  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }
}