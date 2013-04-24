package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.testutil.mock.MockFacade;

public class JsonPropertyConsumerTest {

  @Test
  public void simplePropertyOnOpenReader() throws Exception {
    String simplePropertyJson = "{\"Name\":\"Team 1\"}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams").getEntityType().getProperty("Name");
    EntityPropertyInfo entityPropertyInfo = EntityInfoAggregator.create(edmProperty);
    reader.beginObject();
    reader.nextName();

    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Object value = jpc.readProperty(reader, entityPropertyInfo, null);
    assertEquals("Team 1", value);
  }
  
  @Test
  public void simplePropertyWithNullMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Age\":67}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    final EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    
    Map<String, Object> typeMappings = null;
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> resultMap = jpc.readPropertyStandalone(reader, edmProperty, typeMappings);
    
    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }
  
  @Test
  public void simplePropertyWithEmptyMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Age\":67}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    final EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> resultMap = jpc.readPropertyStandalone(reader, edmProperty, typeMappings);
    
    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }
  
  @Test
  public void simplePropertyWithStringToLongMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Age\":67}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    final EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    typeMappings.put("Age", Long.class);
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> resultMap = jpc.readPropertyStandalone(reader, edmProperty, typeMappings);
    
    assertEquals(Long.valueOf(67), resultMap.get("Age"));
  }
  
  @Test
  public void simplePropertyWithStringToNullMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Age\":67}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    final EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    typeMappings.put("Age", null);
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> resultMap = jpc.readPropertyStandalone(reader, edmProperty, typeMappings);
    
    assertEquals(Integer.valueOf(67), resultMap.get("Age"));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void complexPropertyWithStringToStringMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"}}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmComplexType complexPropertyType = (EdmComplexType) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location").getType();
    EdmProperty edmProperty = (EdmProperty) complexPropertyType.getProperty("City");
   
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> innerMappings = new HashMap<String, Object>();
    innerMappings.put("PostalCode", String.class);
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    typeMappings.put("City", innerMappings);
    Map<String, Object> result = jpc.readPropertyStandalone(reader, edmProperty, typeMappings);

    assertEquals(1, result.size());
    Map<String, Object> innerResult = (Map<String, Object>) result.get("City"); 
    assertEquals("Heidelberg", innerResult.get("CityName"));
    assertEquals("69124", innerResult.get("PostalCode"));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void deepComplexPropertyWithStringToStringMappingStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Location\":{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"},\"Country\":\"Germany\"}}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location");
   
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    
    Map<String, Object> cityMappings = new HashMap<String, Object>();
    cityMappings.put("PostalCode", String.class);
    Map<String, Object> locationMappings = new HashMap<String, Object>();
    locationMappings.put("City", cityMappings);
    Map<String, Object> mappings = new HashMap<String, Object>();
    mappings.put("Location", locationMappings);
    
    Map<String, Object> result = (Map<String, Object>) jpc.readPropertyStandalone(reader, edmProperty, mappings);

    assertEquals(1, result.size());
    Map<String, Object> locationResult = (Map<String, Object>) result.get("Location");
    assertEquals(2, locationResult.size());
    assertEquals("Germany", locationResult.get("Country"));
    Map<String, Object> innerResult = (Map<String, Object>) locationResult.get("City");
    assertEquals(2, innerResult.size());
    assertEquals("Heidelberg", innerResult.get("CityName"));
    assertEquals("69124", innerResult.get("PostalCode"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void complexPropertyOnOpenReader() throws Exception {
    String simplePropertyJson = "{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmComplexType complexPropertyType = (EdmComplexType) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location").getType();
    EdmProperty edmProperty = (EdmProperty) complexPropertyType.getProperty("City");
    EntityComplexPropertyInfo entityPropertyInfo = (EntityComplexPropertyInfo) EntityInfoAggregator.create(edmProperty);

    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = (Map<String, Object>) jpc.readProperty(reader, entityPropertyInfo, null);

    assertEquals(2, result.size());
    assertEquals("Heidelberg", result.get("CityName"));
    assertEquals("69124", result.get("PostalCode"));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void complexPropertyOnOpenReaderWithNoMetadata() throws Exception {
    String simplePropertyJson = "{\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmComplexType complexPropertyType = (EdmComplexType) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location").getType();
    EdmProperty edmProperty = (EdmProperty) complexPropertyType.getProperty("City");
    EntityComplexPropertyInfo entityPropertyInfo = (EntityComplexPropertyInfo) EntityInfoAggregator.create(edmProperty);

    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = (Map<String, Object>) jpc.readProperty(reader, entityPropertyInfo, null);

    assertEquals(2, result.size());
    assertEquals("Heidelberg", result.get("CityName"));
    assertEquals("69124", result.get("PostalCode"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void deepComplexPropertyOnOpenReader() throws Exception {
    String simplePropertyJson = "{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"},\"Country\":\"Germany\"}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location");
    EntityComplexPropertyInfo entityPropertyInfo = (EntityComplexPropertyInfo) EntityInfoAggregator.create(edmProperty);

    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = (Map<String, Object>) jpc.readProperty(reader, entityPropertyInfo, null);

    assertEquals(2, result.size());
    assertEquals("Germany", result.get("Country"));
    Map<String, Object> innerResult = (Map<String, Object>) result.get("City");
    assertEquals(2, innerResult.size());
    assertEquals("Heidelberg", innerResult.get("CityName"));
    assertEquals("69124", innerResult.get("PostalCode"));
  }

  @Test
  public void simplePropertyStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Name\":\"Team 1\"}}";
    EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams").getEntityType().getProperty("Name");
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = jpc.readPropertyStandalone(reader, edmProperty);
    assertEquals("Team 1", result.get("Name"));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void complexPropertyStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"}}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmComplexType complexPropertyType = (EdmComplexType) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location").getType();
    EdmProperty edmProperty = (EdmProperty) complexPropertyType.getProperty("City");
   
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = jpc.readPropertyStandalone(reader, edmProperty);

    assertEquals(1, result.size());
    Map<String, Object> innerResult = (Map<String, Object>) result.get("City"); 
    assertEquals("Heidelberg", innerResult.get("CityName"));
    assertEquals("69124", innerResult.get("PostalCode"));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void deepComplexPropertyStandalone() throws Exception {
    String simplePropertyJson = "{\"d\":{\"Location\":{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"},\"Country\":\"Germany\"}}}";
    InputStream jsonStream = createContentAsStream(simplePropertyJson);
    JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
    EdmProperty edmProperty = (EdmProperty) MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees").getEntityType().getProperty("Location");
   
    JsonPropertyConsumer jpc = new JsonPropertyConsumer();
    Map<String, Object> result = (Map<String, Object>) jpc.readPropertyStandalone(reader, edmProperty, null);

    assertEquals(1, result.size());
    Map<String, Object> locationResult = (Map<String, Object>) result.get("Location");
    assertEquals(2, locationResult.size());
    assertEquals("Germany", locationResult.get("Country"));
    Map<String, Object> innerResult = (Map<String, Object>) locationResult.get("City");
    assertEquals(2, innerResult.size());
    assertEquals("Heidelberg", innerResult.get("CityName"));
    assertEquals("69124", innerResult.get("PostalCode"));
  }

  private InputStream createContentAsStream(final String json) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(json.getBytes("UTF-8"));
  }

}
