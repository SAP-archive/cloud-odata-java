package com.sap.core.odata.core.ep.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

public class XmlPropertySerializationTest extends AbstractSerializerTest {

  @Test
  public void serializeEmployeeId() throws Exception {
    ODataEntityProvider s = createAtomSerializer();
    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeId");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    InputStream in = s.writeProperty(edmProperty, this.employeeData.get("EmployeeId"));
    assertNotNull(in);

    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    assertXpathExists("/d:EmployeeId", xml);
    assertXpathEvaluatesTo("1", "/d:EmployeeId/text()", xml);
  }

  @Test
  public void serializeAge() throws Exception {
    ODataEntityProvider s = createAtomSerializer();

    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    InputStream in = s.writeProperty(edmProperty, this.employeeData.get("Age"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    assertXpathExists("/d:Age", xml);
    assertXpathEvaluatesTo("52", "/d:Age/text()", xml);
    assertXpathExists("/d:Age/@m:type", xml);
    assertXpathEvaluatesTo("Edm.Int32", "/d:Age/@m:type", xml);

  }

  @Test
  public void serializeImageUrl() throws Exception {
    ODataEntityProvider s = createAtomSerializer();

    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("ImageUrl");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    InputStream in = s.writeProperty(edmProperty, this.employeeData.get("ImageUrl"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    assertXpathExists("/d:ImageUrl", xml);
    assertXpathExists("/d:ImageUrl/@m:null", xml);
    assertXpathEvaluatesTo("true", "/d:ImageUrl/@m:null", xml);

  }

  @Test
  public void serializeLocation() throws Exception {
    ODataEntityProvider s = createAtomSerializer();

    EdmEntityType edmEntityType = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee");
    EdmTyped edmTyped = edmEntityType.getProperty("Location");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    InputStream in = s.writeProperty(edmProperty, this.employeeData.get("Location"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    this.log.debug(xml);

    assertXpathExists("/d:Location", xml);
    assertXpathExists("/d:Location/d:City", xml);
    assertXpathExists("/d:Location/d:City/d:PostalCode", xml);
    assertXpathExists("/d:Location/d:City/d:CityName", xml);
    assertXpathExists("/d:Location/d:Country", xml);

    assertXpathEvaluatesTo("33470", "/d:Location/d:City/d:PostalCode/text()", xml);
    assertXpathEvaluatesTo("Duckburg", "/d:Location/d:City/d:CityName/text()", xml);
    assertXpathEvaluatesTo("Calisota", "/d:Location/d:Country/text()", xml);
  }

}
