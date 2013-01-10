package com.sap.core.odata.core.ec;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.core.ec.XmlPropertyConsumer.Property;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlPropertyConsumerTest {

  @Test
  public void testReadIntegerProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");
    
    Property prop = xpc.readProperty(reader, property);
    
    assertEquals("Age", prop.name);
    assertEquals(Integer.valueOf(67), prop.value);
  }

  @Test
  public void testReadStringProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    
    String xml = "<EmployeeName>Max Mustermann</EmployeeName>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EmployeeName");
    
    Property prop = xpc.readProperty(reader, property);
    
    assertEquals("EmployeeName", prop.name);
    assertEquals("Max Mustermann", prop.value);
  }

  private XMLStreamReader createReaderForTest(String input) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));
    
    return streamReader;
  }

}
