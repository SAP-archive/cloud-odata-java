package com.sap.core.odata.core.enums;

import org.junit.Test;
import static org.junit.Assert.*;

import com.sap.core.odata.api.enums.MediaType;

public class MediaTypeTest {

  @Test
  public void testMediaTypeCreation() {
    MediaType mt = MediaType.create("type", "subtype");
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals("type/subtype", mt.toString());
  }

  @Test
  public void testMediaTypeWithParameterCreation() {
    MediaType mt = MediaType.create("type", "subtype").addParameter("key", "value");
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals(1, mt.getParameters().size());
    assertEquals("value", mt.getParameters().get("key"));
    assertEquals("type/subtype;key=value", mt.toString());
  }
  
  @Test
  public void testMediaTypeWithParametersCreation() {
    MediaType mt = MediaType.create("type", "subtype").addParameter("key1", "value1").addParameter("key2", "value2");
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals(2, mt.getParameters().size());
    assertEquals("value1", mt.getParameters().get("key1"));
    assertEquals("value2", mt.getParameters().get("key2"));
    assertEquals("type/subtype;key1=value1;key2=value2", mt.toString());
  }
}
