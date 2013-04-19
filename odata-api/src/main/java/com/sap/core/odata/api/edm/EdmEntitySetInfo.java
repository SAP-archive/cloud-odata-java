package com.sap.core.odata.api.edm;

import java.net.URI;

/**
 * @com.sap.core.odata.DoNotImplement
 * Objects of this class contain information about one entity set inside the EntityDataModel.
 * @author SAP AG
 *
 */
public interface EdmEntitySetInfo {

  /**
   * @return the entity container name which contains this entity set.
   */
  public String getEntityContainerName();

  /**
   * @return the entity set name
   */
  public String getEntitySetName();

  /**
   * @return true if this entity set is contained inside the default container
   */
  public boolean isDefaultEntityContainer();

  /**
   * We use a {@link URI} object here to ensure the right encoding. If a string representation is needed the toASCIIString() method can be used.
   * @return the uri to this entity set e.g. "Employees"
   */
  public URI getEntitySetUri();

}
