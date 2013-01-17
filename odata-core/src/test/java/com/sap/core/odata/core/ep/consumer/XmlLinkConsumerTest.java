package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;

public class XmlLinkConsumerTest {

  // -> http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Managers('1')/$links/nm_Employees
  
  String MANAGER_1_EMPLOYEES = 
      "<links xmlns=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">" +
        "<uri>http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Employees('1')</uri>" +
        "<uri>http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Employees('2')</uri>" +
        "<uri>http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Employees('3')</uri>" +
        "<uri>http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Employees('6')</uri>" +
      "</links>";
  
  @Test
  public void testReadLink() throws Exception {
    XmlLinkConsumer xlc = new XmlLinkConsumer();
    
    XMLStreamReader reader = null;
    EdmEntitySet entitySet = null;
    String link = xlc.readLink(reader, entitySet);

    assertEquals(null, link);
  }

  @Test
  public void testReadLinks() throws Exception {
    XmlLinkConsumer xlc = new XmlLinkConsumer();
    
    XMLStreamReader reader = null;
    EdmEntitySet entitySet = null;
    List<String> links = xlc.readLinks(reader, entitySet);

    assertEquals(0, links.size());
  }
}
