/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.UriNotMatchingException;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class MessageReferenceTest extends BaseTest {

  @Test
  public void testAddContent() {
    String content = "content";
    ODataMessageException e = new UriNotMatchingException(UriNotMatchingException.ENTITYNOTFOUND.addContent(content));

    assertEquals(1, e.getMessageReference().getContent().size());
  }

  @Test
  public void testAddContentMoreThanOnce() {
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
