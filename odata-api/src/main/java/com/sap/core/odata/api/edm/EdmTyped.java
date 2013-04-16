package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmTyped indicates if an EDM element is of a special type and holds the multiplicity of that type.
 * @author SAP AG
 */
public interface EdmTyped extends EdmNamed {

  /**
   * See {@link EdmType} for more information about possible types.
   * 
   * @return {@link EdmType}
   * @throws EdmException
   */
  EdmType getType() throws EdmException;

  /**
   * See {@link EdmMultiplicity} for more information about possible multiplicities.
   * 
   * @return {@link EdmMultiplicity}
   * @throws EdmException
   */
  EdmMultiplicity getMultiplicity() throws EdmException;
}