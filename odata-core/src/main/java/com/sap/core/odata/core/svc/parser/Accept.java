package com.sap.core.odata.core.svc.parser;

/**
 * An Accept element
 * <p>Accept element indicates the types of representation accepted by the Collection
 * @author SAP AG
 */
public interface Accept {

  /**
   * Get the media range
   * 
   * @return value as String
   */
  public String getValue();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();
}
