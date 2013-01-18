package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.mock.MockFacade;

public class ProviderFacadeImplTest {

  public static final String EMPLOYEE_1_XML = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\">" + 
        "<id>https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')</id>" +
        "<title type=\"text\">Walter Winter</title>" + 
        "<updated>1999-01-01T00:00:00Z</updated>" + 
        "<category term=\"RefScenario.Employee\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" + 
        "<link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/><link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\"/>" + 
        "<link href=\"Employees('1')/ne_Room\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Room\" type=\"application/atom+xml; type=entry\" title=\"ne_Room\"/>" + 
        "<link href=\"Employees('1')/ne_Manager\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Manager\" type=\"application/atom+xml; type=entry\" title=\"ne_Manager\"/>" + 
        "<link href=\"Employees('1')/ne_Team\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Team\" type=\"application/atom+xml; type=entry\" title=\"ne_Team\"/>" + 
        "<content type=\"application/octet-stream\" src=\"Employees('1')/$value\"/>" + 
        "<m:properties>" + 
          "<d:EmployeeId>1</d:EmployeeId>" + 
          "<d:EmployeeName>Walter Winter</d:EmployeeName>" + 
          "<d:ManagerId>1</d:ManagerId>" + 
          "<d:RoomId>1</d:RoomId>" + 
          "<d:TeamId>1</d:TeamId>" + 
          "<d:Location m:type=\"RefScenario.c_Location\">" + 
            "<d:Country>Germany</d:Country>" + 
            "<d:City m:type=\"RefScenario.c_City\">" + 
              "<d:PostalCode>69124</d:PostalCode>" + 
              "<d:CityName>Heidelberg</d:CityName>" + 
            "</d:City>" + 
          "</d:Location>" + 
          "<d:Age>52</d:Age>" + 
          "<d:EntryDate>1999-01-01T00:00:00</d:EntryDate>" + 
          "<d:ImageUrl>/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg</d:ImageUrl>" + 
        "</m:properties>" + 
      "</entry>";

  @Test
  public void testReadEntry() throws Exception {
    ProviderFacadeImpl provider = new ProviderFacadeImpl();
    
    String contentType = ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    Object result = provider.readEntry(contentType, entitySet, content);

    assertTrue(result instanceof ODataEntry);
  }

  @Test
  @Ignore
  public void testReadPropertyValue() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteServiceDocument() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWritePropertyValue() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteText() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteBinary() {
    fail("Not yet implemented");
  }


  @Test
  @Ignore
  public void testReadLink() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testReadProperty() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testReadLinks() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteFeed() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteEntry() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteProperty() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteLink() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteLinks() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteFunctionImport() {
    fail("Not yet implemented");
  }

}
