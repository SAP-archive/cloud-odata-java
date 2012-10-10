package org.odata4j.test.unit.edm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;

public class EdmPropertyTest
{
  private static final String MIMETYPE = "image/jpeg";
  private static final String CONCURRENCYMODE = "Fixed";
  private static final String NSURI = "http://localhost";
  private static final String NSPREFIX = "de";

  @Test
  public void edmPropertyMimeType() {
    EdmProperty.Builder builder = EdmProperty.newBuilder("Name");
    EdmProperty property = builder.setMimeType(MIMETYPE).setType(EdmSimpleType.STRING).build();
    assertEquals(MIMETYPE, property.getMimeType());

  }

  @Test
  public void edmPropertyConcurrencyMode() {
    EdmProperty.Builder builder = EdmProperty.newBuilder("Name");
    EdmProperty property = builder.setConcurrencyMode(CONCURRENCYMODE).setType(EdmSimpleType.STRING).build();
    assertEquals(CONCURRENCYMODE, property.getConcurrencyMode());
  }

  @Test
  public void edmPropertyFCNsUri() {
    EdmProperty.Builder builder = EdmProperty.newBuilder("Name");
    EdmProperty property = builder.setFcNsUri(NSURI).setType(EdmSimpleType.STRING).build();
    assertEquals(NSURI, property.getFcNsUri());
  }

  @Test
  public void edmPropertyFCNsPrefix() {
    EdmProperty.Builder builder = EdmProperty.newBuilder("Name");
    EdmProperty property = builder.setFcNsPrefix(NSPREFIX).setType(EdmSimpleType.STRING).build();
    assertEquals(NSPREFIX, property.getFcNsPrefix());
  }

}