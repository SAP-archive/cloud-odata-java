package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class BasicProviderTest extends AbstractProviderTest {

  protected static BasicEntityProviderInterface provider = new BasicEntityProvider();

  @Test
  public void writePropertyValue() throws Exception {
    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataResponse response = provider.writePropertyValue(edmProperty, this.employeeData.get("Age"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.TEXT_PLAIN.toString(), response.getContentHeader());
    String value = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(this.employeeData.get("Age").toString(), value);
  }

  @Test
  public void readPropertyValue() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    final Integer age = (Integer) provider.readPropertyValue(property, new ByteArrayInputStream("42".getBytes("UTF-8")));
    assertEquals(Integer.valueOf(42), age);
  }

  @Test
  public void readPropertyBinaryValue() throws Exception {
    final byte[] bytes = new byte[]{1, 2, 3, 4, -128};
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario2", "Photo").getProperty("Image");

    assertTrue(Arrays.equals(bytes, (byte[]) provider.readPropertyValue(property, new ByteArrayInputStream(bytes))));
  }

  @Test
  public void writeBinary() throws Exception {
    final byte[] bytes = new byte[]{49, 50, 51, 52, 65};
    final ODataResponse response = provider.writeBinary(ContentType.TEXT_PLAIN.toString(), bytes);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.TEXT_PLAIN.toString(), response.getContentHeader());
    final String value = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals("1234A", value);
  }

  @Test
  public void readBinary() throws Exception {
    final byte[] bytes = new byte[]{1, 2, 3, 4, -128};
    assertTrue(Arrays.equals(bytes, provider.readBinary(new ByteArrayInputStream(bytes))));
  }
}
