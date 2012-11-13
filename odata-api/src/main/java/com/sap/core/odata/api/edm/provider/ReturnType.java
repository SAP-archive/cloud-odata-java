package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

public class ReturnType {

  private FullQualifiedName qualifiedName;
  private EdmMultiplicity multiplicity;

  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  public ReturnType setQualifiedName(FullQualifiedName qualifiedName) {
    this.qualifiedName = qualifiedName;
    return this;
  }

  public ReturnType setMultiplicity(EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }
}