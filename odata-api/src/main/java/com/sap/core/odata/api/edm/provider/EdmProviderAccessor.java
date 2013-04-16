package com.sap.core.odata.api.edm.provider;

/**
 * This interface can be used to access the {@link EdmProvider} within an application. 
 * @author SAP AG
 *
 */
public interface EdmProviderAccessor {

  /**
   * @return {@link EdmProvider} of this service
   */
  public EdmProvider getEdmProvider();

}
