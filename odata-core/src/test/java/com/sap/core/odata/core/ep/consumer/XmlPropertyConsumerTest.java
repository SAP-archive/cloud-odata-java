package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlPropertyConsumerTest extends AbstractConsumerTest {

  @Test
  public void readIntegerProperty() throws Exception {
    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Age>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void readIntegerPropertyAsLong() throws Exception {
    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Age>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    Map<String, Object> typeMappings = createTypeMappings("Age", Long.class);
    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false, typeMappings);

    assertEquals(Long.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void readIntegerPropertyWithNullMapping() throws Exception {
    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Age>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    Map<String, Object> typeMappings = null;
    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false, typeMappings);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void readIntegerPropertyWithEmptyMapping() throws Exception {
    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Age>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    Map<String, Object> typeMappings = new HashMap<String, Object>();
    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false, typeMappings);

    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }

  @Test
  public void readStringProperty() throws Exception {
    String xml = "<EmployeeName xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">Max Mustermann</EmployeeName>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertEquals("Max Mustermann", resultMap.get("EmployeeName"));
  }

  @Test
  public void readStringPropertyEmpty() throws Exception {
    final String xml = "<EmployeeName xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    final Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertTrue(resultMap.containsKey("EmployeeName"));
    assertEquals("", resultMap.get("EmployeeName"));
  }

  @Test
  public void readStringPropertyNull() throws Exception {
    final String xml = "<EntryDate xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"true\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");

    final Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertTrue(resultMap.containsKey("EntryDate"));
    assertNull(resultMap.get("EntryDate"));
  }

  @Test
  public void readStringPropertyNullFalse() throws Exception {
    final String xml = "<EntryDate xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"false\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">1970-01-02T00:00:00</EntryDate>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");

    final Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertEquals(86400000L, ((Calendar) resultMap.get("EntryDate")).getTimeInMillis());
  }

  @Test(expected = EntityProviderException.class)
  public void invalidSimplePropertyName() throws Exception {
    final String xml = "<Invalid xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Invalid>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void invalidNullAttribute() throws Exception {
    final String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"wrong\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void nullValueNotAllowed() throws Exception {
    final String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"true\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.isNullable()).thenReturn(false);
    when(property.getFacets()).thenReturn(facets);

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void readComplexProperty() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void readComplexPropertyWithLineBreaks() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "    " +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "\n" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>" +
            "\n        \n ";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test(expected = EntityProviderException.class)
  public void readComplexPropertyInvalidMapping() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    try {
      Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false,
          createTypeMappings("Location", createTypeMappings("City", createTypeMappings("PostalCode", Integer.class))));

      //      Map<String, Object> resultMap = xpc.readProperty(reader, property, false, 
      //          createTypeMappings("PostalCode", Integer.class));
      Assert.assertNotNull(resultMap);
    } catch (EntityProviderException e) {
      Assert.assertTrue(e.getCause() instanceof EdmSimpleTypeException);
      throw e;
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void readComplexPropertyWithMappings() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "  <PostalCode>69124</PostalCode>" +
            "  <CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);

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
    when(cityComplexType.getName()).thenReturn("c_City");
    when(cityComplexType.getNamespace()).thenReturn("RefScenario");
    when(cityComplexType.getPropertyNames()).thenReturn(Arrays.asList("PostalCode", "CityName"));

    final EdmProperty cityProperty = mock(EdmProperty.class);
    when(cityProperty.getType()).thenReturn(cityComplexType);
    when(cityProperty.getName()).thenReturn("City");
    when(locationComplexType.getProperty("City")).thenReturn(cityProperty);

    createProperty("PostalCode", EdmSimpleTypeKind.Int32, cityComplexType);
    createProperty("CityName", EdmSimpleTypeKind.String, cityComplexType);

    // Execute test
    Map<String, Object> typeMappings =
        createTypeMappings("Location",
            createTypeMappings("City",
                createTypeMappings("CityName", String.class, "PostalCode", Long.class)));
    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, locationComplexProperty, false, typeMappings);

    // verify
    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals(Long.valueOf("69124"), cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void readComplexPropertyWithNamespace() throws Exception {
    String xml =
        "<d:Location m:type=\"RefScenario.c_Location\" " +
            "    xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\"" +
            "    xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\">" +
            "  <d:Country>Germany</d:Country>" +
            "  <d:City m:type=\"RefScenario.c_City\">" +
            "    <d:PostalCode>69124</d:PostalCode>" +
            "    <d:CityName>Heidelberg</d:CityName>" +
            "  </d:City>" +
            "</d:Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    Object prop = new XmlPropertyConsumer().readProperty(reader, property, false);
    Map<String, Object> resultMap = (Map<String, Object>) prop;

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test(expected = EntityProviderException.class)
  public void readComplexPropertyWithInvalidChild() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "<Invalid>Germany</Invalid>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void readComplexPropertyWithInvalidDeepChild() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<Invalid>69124</Invalid>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void readComplexPropertyWithInvalidName() throws Exception {
    String xml =
        "<Invalid xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"RefScenario.c_Location\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Invalid>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void readComplexPropertyWithInvalidTypeAttribute() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" m:type=\"Invalid\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void readComplexPropertyWithoutTypeAttribute() throws Exception {
    String xml =
        "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\""
            + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">" +
            "<Country>Germany</Country>" +
            "<City m:type=\"RefScenario.c_City\">" +
            "<PostalCode>69124</PostalCode>" +
            "<CityName>Heidelberg</CityName>" +
            "</City>" +
            "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    Map<String, Object> locationMap = (Map<String, Object>) resultMap.get("Location");
    assertEquals("Germany", locationMap.get("Country"));
    Map<String, Object> cityMap = (Map<String, Object>) locationMap.get("City");
    assertEquals("69124", cityMap.get("PostalCode"));
    assertEquals("Heidelberg", cityMap.get("CityName"));
  }

  @Test
  public void complexPropertyNull() throws Exception {
    String xml = "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"true\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    final Map<String, Object> resultMap = new XmlPropertyConsumer().readProperty(reader, property, false);

    assertTrue(resultMap.containsKey("Location"));
    assertNull(resultMap.get("Location"));
  }

  @Test(expected = EntityProviderException.class)
  public void complexPropertyNullValueNotAllowed() throws Exception {
    final String xml = "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"true\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\" />";
    XMLStreamReader reader = createReaderForTest(xml, true);
    EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.isNullable()).thenReturn(false);
    when(property.getFacets()).thenReturn(facets);

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  @Test(expected = EntityProviderException.class)
  public void complexPropertyNullWithContent() throws Exception {
    String xml = "<Location xmlns=\"" + Edm.NAMESPACE_D_2007_08
        + "\" m:null=\"true\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">"
        + "<City><PostalCode/><CityName/></City><Country>Germany</Country>"
        + "</Location>";
    XMLStreamReader reader = createReaderForTest(xml, true);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");

    new XmlPropertyConsumer().readProperty(reader, property, false);
  }

  private static EdmProperty createProperty(final String name, final EdmSimpleTypeKind kind, final EdmStructuralType entityType) throws EdmException {
    final EdmProperty property = mock(EdmProperty.class);
    when(property.getType()).thenReturn(kind.getEdmSimpleTypeInstance());
    when(property.getName()).thenReturn(name);
    when(entityType.getProperty(name)).thenReturn(property);
    return property;
  }
}
