package com.sap.core.odata.testutil.mock;

import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;

/**
 * Helper for the entity data model used as technical reference scenario.
 * @author SAP AG
 */
public class TecEdmInfo {
  private final Edm edm;

  public TecEdmInfo(final Edm edm) {
    this.edm = edm;
  }

  public EdmEntityType getTypeEtAllTypes() {
    try {
      return edm
          .getEntityContainer(TechnicalScenarioEdmProvider.ENTITY_CONTAINER_1)
          .getEntitySet(TechnicalScenarioEdmProvider.ES_ALL_TYPES)
          .getEntityType();
    } catch (final EdmException e) {
      fail("Error in test setup" + e.getLocalizedMessage());
    }
    return null;
  }
}
