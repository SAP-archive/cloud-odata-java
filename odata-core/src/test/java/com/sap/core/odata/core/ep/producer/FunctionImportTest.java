package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class FunctionImportTest extends AbstractProviderTest {

  @Test
  public void singleSimpleType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("MaximalAge");

    final ODataResponse response = createAtomEntityProvider().writeFunctionImport(functionImport, employeeData.get("Age"), DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:MaximalAge", xml);
    assertXpathEvaluatesTo("52", "/d:MaximalAge/text()", xml);
  }

  @Test
  public void singleComplexType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("MostCommonLocation");

    final ODataResponse response = createAtomEntityProvider().writeFunctionImport(functionImport, employeeData.get("Location"), DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:MostCommonLocation", xml);
    assertXpathEvaluatesTo("RefScenario.c_Location", "/d:MostCommonLocation/@m:type", xml);
    assertXpathEvaluatesTo("Duckburg", "/d:MostCommonLocation/d:City/d:CityName/text()", xml);
  }

  @Test
  public void collectionOfSimpleTypes() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("AllUsedRoomIds");

    final ODataResponse response = createAtomEntityProvider().writeFunctionImport(functionImport, Arrays.asList("1", "2", "3"), DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:AllUsedRoomIds", xml);
    assertXpathEvaluatesTo("1", "/d:AllUsedRoomIds/d:element/text()", xml);
    assertXpathEvaluatesTo("2", "/d:AllUsedRoomIds/d:element[2]/text()", xml);
    assertXpathEvaluatesTo("3", "/d:AllUsedRoomIds/d:element[3]/text()", xml);
  }

  @Test
  public void collectionOfComplexTypes() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("AllLocations");

    final ODataResponse response = createAtomEntityProvider().writeFunctionImport(functionImport, Arrays.asList(employeeData.get("Location")), DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:AllLocations", xml);
    assertXpathExists("/d:AllLocations/d:element", xml);
    assertXpathEvaluatesTo("RefScenario.c_Location", "/d:AllLocations/d:element/@m:type", xml);
    assertXpathEvaluatesTo("Duckburg", "/d:AllLocations/d:element/d:City/d:CityName/text()", xml);
  }

  @Test
  public void singleEntityType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("OldestEmployee");

    final ODataResponse response = createAtomEntityProvider().writeFunctionImport(functionImport, employeeData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_ATOM_XML_ENTRY.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/a:entry", xml);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/$value\"]", xml);
    assertXpathEvaluatesTo("Duckburg", "/a:entry/m:properties/d:Location/d:City/d:CityName/text()", xml);
  }
}
