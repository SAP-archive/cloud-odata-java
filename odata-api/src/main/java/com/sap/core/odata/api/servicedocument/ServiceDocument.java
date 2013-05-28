package com.sap.core.odata.api.servicedocument;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * A Service document
 * <p>Service document lists all EntitySets
 * @author SAP AG
 */
public interface ServiceDocument {
  /**
   * Get the list of the EntitySets
   * 
   * @return a list of {@link EdmEntitySetInfo}
   */
  public List<EdmEntitySetInfo> getEntitySetsInfo() throws EntityProviderException;

  /**
   * Get additional information if the service document is in atom format 
   * 
   * @return {@link AtomInfo} or null
   */
  public AtomInfo getAtomInfo();
}
