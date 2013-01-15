package com.sap.core.odata.core.ep.aggregator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.core.ep.AbstractProviderTest;
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
    EntityPropertyInfo propertyInfoAge = eia.getPropertyInfo("Age");
    assertFalse(propertyInfoAge.isComplex());
    assertEquals("Age", propertyInfoAge.getName());
    assertEquals("Int32", propertyInfoAge.getType().getName());
    EntityPropertyInfo propertyInfoLocation = eia.getPropertyInfo("Location");
    assertTrue(propertyInfoLocation.isComplex());
    assertEquals("Location", propertyInfoLocation.getName());
    EntityComplexPropertyInfo comPropInfoLocation = (EntityComplexPropertyInfo) propertyInfoLocation;
    assertEquals(2, comPropInfoLocation.getPropertyInfos().size());
  }
}
