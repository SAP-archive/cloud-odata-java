package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 *
 */
public class ReturnType {

  private FullQualifiedName qualifiedName;
  private EdmMultiplicity multiplicity;

  /**
   * @return
   */
  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  /**
   * @return
   */
  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * @param qualifiedName
   * @return
   */
  public ReturnType setQualifiedName(FullQualifiedName qualifiedName) {
    this.qualifiedName = qualifiedName;
    return this;
  }

  /**
   * @param multiplicity
   * @return
   */
  public ReturnType setMultiplicity(EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }
}