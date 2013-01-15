package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlPropertyConsumerTest extends BaseTest {

  @Test
  public void testReadIntegerProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");
    
    Map<String, Object> resultMap = xpc.readProperty(reader, property);
    
    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void testReadStringProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = "<EmployeeName>Max Mustermann</EmployeeName>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EmployeeName");
    
    Map<String, Object> resultMap = xpc.readProperty(reader, property);

    assertEquals("Max Mustermann", resultMap.get("EmployeeName"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testReadComplexProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = 
        "<Location m:type=\"RefScenario.c_Location\">" + 
            "<Country>Germany</Country>" + 
            "<City m:type=\"RefScenario.c_City\">" + 
              "<PostalCode>69124</PostalCode>" + 
              "<CityName>Heidelberg</CityName>" + 
            "</City>" + 
          "</Location>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Location");
    
    Map<String, Object> resultMap = xpc.readProperty(reader, property);
    
    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @SuppressWarnings("unchecked")
  @Test
  @Ignore
  public void testReadComplexPropertyWithNamespace() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = 
        "<d:Location m:type=\"RefScenario.c_Location\">" + 
            "<d:Country>Germany</d:Country>" + 
            "<d:City m:type=\"RefScenario.c_City\">" + 
              "<d:PostalCode>69124</d:PostalCode>" + 
              "<d:CityName>Heidelberg</d:CityName>" + 
            "</d:City>" + 
          "</d:Location>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Location");
    
    Object prop = xpc.readProperty(reader, property);
    Map<String, Object> resultMap = (Map<String, Object>) prop;
    
    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  private XMLStreamReader createReaderForTest(String input) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));
    
    return streamReader;
  }
}
