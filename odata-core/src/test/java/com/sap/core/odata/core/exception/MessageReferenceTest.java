/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
