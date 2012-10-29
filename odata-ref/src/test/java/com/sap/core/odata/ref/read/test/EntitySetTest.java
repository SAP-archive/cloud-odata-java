package com.sap.core.odata.ref.read.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.ref.processor.ScenarioProcessor;

public class EntitySetTest {
    
  @Test
  public void readEmployees() throws Exception{
    ScenarioProcessor processor = new ScenarioProcessor();
   
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Employees");
    
    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
        
    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertEquals("Employees", response.getEntity());
  }

  
  @Test
  public void readManagers() throws Exception{
    ScenarioProcessor processor = new ScenarioProcessor();
   
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Managers");
    
    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    
    
    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertEquals("Managers", response.getEntity());
  }
  
  @Test
  public void readBuildings() throws Exception{
    ScenarioProcessor processor = new ScenarioProcessor();
   
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Buildings");
    
    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    
    
    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertEquals("Buildings", response.getEntity());
  }
  
  @Test
  public void readTeams() throws Exception{
    ScenarioProcessor processor = new ScenarioProcessor();
   
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Teams");
    
    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    
    
    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertEquals("Teams", response.getEntity());
  }
  
  @Test
  public void readRooms() throws Exception{
    ScenarioProcessor processor = new ScenarioProcessor();
   
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Rooms");
    
    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    
    
    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertEquals("Rooms", response.getEntity());
  }
}
