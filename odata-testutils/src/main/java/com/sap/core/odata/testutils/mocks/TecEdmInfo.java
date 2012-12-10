package com.sap.core.odata.testutils.mocks;

import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;

/**
 * Provider for the entity data model used in the reference scenario
 * @author SAP AG
 */
public class TecEdmInfo
{
  private Edm edm;

  public TecEdmInfo(Edm edm)
  {
    this.edm = edm;
  }
  
  public EdmEntityType getEtAllTypes()
  {
    try {
      return edm
          .getEntityContainer(TechnicalScenarioEdmProvider.ENTITY_CONTAINER_1)
          .getEntitySet(TechnicalScenarioEdmProvider.ES_ALL_TYPES)
          .getEntityType();
    } catch (EdmException e) {
      fail("Error in test setup" + e.getLocalizedMessage());
    }
    return null;
  }
}
