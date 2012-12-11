package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.testutils.mocks.MockFacade;


public class EntityInfoAggregatorTest extends AbstractProviderTest {

  @Test
  public void simpleTest() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");

    EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
    
    log.debug("Result:\n\t" + eia.getPropertyNames());
    log.debug("Result:\n\t" + eia.getTargetPathNames());
    log.debug("Result:\n\t" + eia.getNavigationPropertyNames());
    
    assertNotNull(eia);
  }
  
//  @Test
//  public void simpleEtagTest() throws Exception {
//    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
//    Map<String, Object> data = this.employeeData;
//    DataContainer dc = new DataContainer(entitySet, data);
//    ODataItem it = dc.init();
//
//    log.debug("Result:\n\t" + it.toString());
//    
//    assertNotNull(it);
//    assertEquals("", dc.createETag());
//  }
}
