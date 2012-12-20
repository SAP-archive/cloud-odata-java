package com.sap.core.odata.core.exception;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.UriNotMatchingException;

public class MessageReferenceTest {

  @Test
  public void testAddContent() {
    String content = "content";
    ODataMessageException e = new UriNotMatchingException(UriNotMatchingException.ENTITYNOTFOUND.addContent(content));
    
    assertEquals(1, e.getMessageReference().getContent().size());
  }

  @Test
  public void testAddContentMoreThenOnce() {
    String content = "content";
    ODataMessageException e = new UriNotMatchingException(UriNotMatchingException.ENTITYNOTFOUND.addContent(content));
    assertEquals(1, e.getMessageReference().getContent().size());

    ODataMessageException e2 = new UriNotMatchingException(UriNotMatchingException.ENTITYNOTFOUND.addContent(content));
    assertEquals(1, e.getMessageReference().getContent().size());
    assertEquals(1, e2.getMessageReference().getContent().size());
  }
  
  @Test
  public void testAddMoreContent() {
    String content = "content";
    ODataMessageException e = new UriNotMatchingException(
        UriNotMatchingException.ENTITYNOTFOUND.addContent(content).addContent("content_2"));
    assertEquals(2, e.getMessageReference().getContent().size());
    assertTrue(e.getMessageReference().getContent().contains("content"));
    assertTrue(e.getMessageReference().getContent().contains("content_2"));

    ODataMessageException e2 = new UriNotMatchingException(UriNotMatchingException.ENTITYNOTFOUND.addContent("content_3"));
    assertEquals(2, e.getMessageReference().getContent().size());
    assertTrue(e.getMessageReference().getContent().contains("content"));
    assertTrue(e.getMessageReference().getContent().contains("content_2"));
    assertEquals(1, e2.getMessageReference().getContent().size());
    assertTrue(e2.getMessageReference().getContent().contains("content_3"));
  }
}
