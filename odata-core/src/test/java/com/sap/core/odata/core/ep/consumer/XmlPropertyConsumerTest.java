package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlPropertyConsumerTest extends AbstractConsumerTest {

  @Test
  public void testReadIntegerProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    Map<String, Object> resultMap = xpc.readProperty(reader, property, false);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void testReadIntegerPropertyAsLong() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    Map<String, Class<?>> typeMappings = createTypeMappings("Age", Long.class);
    Map<String, Object> resultMap = xpc.readProperty(reader, property, false, typeMappings);

    assertEquals(Long.valueOf(67), resultMap.get("Age"));
  }
  
  @Test
  public void testReadIntegerPropertyWithNullMapping() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    Map<String, Class<?>> typeMappings = null;
    Map<String, Object> resultMap = xpc.readProperty(reader, property, false, typeMappings);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }
  
  @Test
  public void testReadIntegerPropertyWithEmptyMapping() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<Age>67</Age>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    Map<String, Class<?>> typeMappings = new HashMap<String, Class<?>>();
    Map<String, Object> resultMap = xpc.readProperty(reader, property, false, typeMappings);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void testReadStringProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<EmployeeName>Max Mustermann</EmployeeName>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EmployeeName");

    Map<String, Object> resultMap = xpc.readProperty(reader, property, false);

    assertEquals("Max Mustermann", resultMap.get("EmployeeName"));
  }

  @Test
  public void testReadStringNullProperty() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml = "<EntryDate m:null=\"true\"/>";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EntryDate");

    Map<String, Object> resultMap = xpc.readProperty(reader, property, false);

    assertEquals(null, resultMap.get("EntryDate"));
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

    Map<String, Object> resultMap = xpc.readProperty(reader, property, false);

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testReadComplexPropertyWithLineBreaks() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml =
        "<Location m:type=\"RefScenario.c_Location\">" +
            "    " +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "\n" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>" +
            "\n        \n ";
    XMLStreamReader reader = createReaderForTest(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Location");

    Map<String, Object> resultMap = xpc.readProperty(reader, property, false);

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test(expected=EntityProviderException.class)
  public void testReadComplexPropertyInvalidMapping() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml =
        "<Location type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Location");

    try {
      Map<String, Object> resultMap = xpc.readProperty(reader, property, false, 
          createTypeMappings("PostalCode", Integer.class));
      Assert.assertNotNull(resultMap);
    } catch(EntityProviderException e) {
      Assert.assertTrue(e.getCause() instanceof EdmSimpleTypeException);
      throw e;
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testReadComplexPropertyWithMappings() throws Exception {
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

    final EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(locationComplexType.getName()).thenReturn("c_Location");
    when(locationComplexType.getNamespace()).thenReturn("RefScenario");
    when(locationComplexType.getPropertyNames()).thenReturn(Arrays.asList("City", "Country"));

    final EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    createProperty("Country", EdmSimpleTypeKind.String, locationComplexType);

    final EdmComplexType cityComplexType = mock(EdmComplexType.class);
    when(cityComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(cityComplexType.getName()).thenReturn("City");
    when(cityComplexType.getNamespace()).thenReturn("RefScenario");
    when(cityComplexType.getPropertyNames()).thenReturn(Arrays.asList("PostalCode", "CityName"));

    final EdmProperty cityProperty = mock(EdmProperty.class);
    when(cityProperty.getType()).thenReturn(cityComplexType);
    when(cityProperty.getName()).thenReturn("City");
    when(locationComplexType.getProperty("City")).thenReturn(cityProperty);

    createProperty("PostalCode", EdmSimpleTypeKind.Int32, cityComplexType);
    createProperty("CityName", EdmSimpleTypeKind.String, cityComplexType);

    
    // Execute test
    Map<String, Class<?>> typeMappings = createTypeMappings(
        "PostalCode", Long.class, 
        "CityName", String.class);
    Map<String, Object> resultMap = xpc.readProperty(reader, locationComplexProperty, false, typeMappings);

    // verify
    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals(Long.valueOf("69124"), cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }


  @SuppressWarnings("unchecked")
  @Test
  public void testReadComplexPropertyWithNamespace() throws Exception {
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String xml =
        "<d:Location m:type=\"RefScenario.c_Location\" " +
        "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
        "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">" +
        "  <d:Country>Germany</d:Country>" +
        "  <d:City m:type=\"RefScenario.c_City\">" +
        "    <d:PostalCode>69124</d:PostalCode>" +
        "    <d:CityName>Heidelberg</d:CityName>" +
        "  </d:City>" +
        "</d:Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Location");

    Object prop = xpc.readProperty(reader, property, false);
    Map<String, Object> resultMap = (Map<String, Object>) prop;

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }
  
  
  private static EdmProperty createProperty(final String name, final EdmSimpleTypeKind kind, final EdmStructuralType entityType) throws EdmException {
    final EdmProperty property = mock(EdmProperty.class);
    when(property.getType()).thenReturn(kind.getEdmSimpleTypeInstance());
    when(property.getName()).thenReturn(name);
    when(entityType.getProperty(name)).thenReturn(property);
    return property;
  }
}
