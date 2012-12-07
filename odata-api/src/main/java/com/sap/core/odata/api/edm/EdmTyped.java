package com.sap.core.odata.api.edm;

/**
 * EdmTyped indicates if an edm element is of a special type and holds the multiplicity of that type.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmTyped extends EdmNamed {

  /**
   * Get the type
   * 
   * @return {@link EdmType}
   * @throws EdmException
   */
  EdmType getType() throws EdmException;

  /**
   * Get the multiplicity
   * 
   * @return {@link EdmMultiplicity}
   * @throws EdmException
   */
  EdmMultiplicity getMultiplicity() throws EdmException;
}