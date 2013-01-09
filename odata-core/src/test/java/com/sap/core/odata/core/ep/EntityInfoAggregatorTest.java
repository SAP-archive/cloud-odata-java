package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class EntityInfoAggregatorTest extends AbstractProviderTest {

  @Test
  public void testEntitySet() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");

    EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
    assertNotNull(eia);
  }
}
