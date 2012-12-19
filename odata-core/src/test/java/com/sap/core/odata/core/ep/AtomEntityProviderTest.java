package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

public class AtomEntityProviderTest extends AbstractProviderTest {

  @Test
  public void writeText() throws Exception {
    ODataEntityProvider s = createAtomEntityProvider();

    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataEntityContent content = s.writeText(edmProperty, this.employeeData.get("Age"));
    assertNotNull(content);
    assertNotNull(content.getContent());
    assertEquals(ContentType.TEXT_PLAIN.toString(), content.getContentHeader());
    String value = StringHelper.inputStreamToString(content.getContent());
    assertEquals(this.employeeData.get("Age").toString(), value);
  }

}
