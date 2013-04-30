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
package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class AtomFeedProducerTest extends AbstractProviderTest {

  public AtomFeedProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  private GetEntitySetUriInfo view;

  @Before
  public void before() throws Exception {
    initializeRoomData(1);

    view = mock(GetEntitySetUriInfo.class);

    EdmEntitySet set = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    when(view.getTargetEntitySet()).thenReturn(set);
  }

  @Test
  public void testFeedNamespaces() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    //EntityProviderProperties properties = EntityProviderProperties.baseUri(BASE_URI).mediaResourceMimeType("mediatype").inlineCountType(view.getInlineCount()).build();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:feed/@xml:base", xmlString);
  }

  @Test
  public void testSelfLink() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/a:link[@rel='self']", xmlString);
    assertXpathEvaluatesTo("Rooms", "/a:feed/a:link[@rel='self']/@href", xmlString);
    assertXpathEvaluatesTo("Rooms", "/a:feed/a:link[@rel='self']/@title", xmlString);
  }

  @Test
  public void testCustomSelfLink() throws Exception {
    String customLink = "Test";
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").selfLink(new URI(customLink)).build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/a:link[@rel='self']", xmlString);
    assertXpathEvaluatesTo(customLink, "/a:feed/a:link[@rel='self']/@href", xmlString);
    assertXpathEvaluatesTo("Rooms", "/a:feed/a:link[@rel='self']/@title", xmlString);
  }

  @Test
  public void testFeedMandatoryParts() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Rooms", "/a:feed/a:id/text()", xmlString);

    assertXpathExists("/a:feed/a:title", xmlString);
    assertXpathEvaluatesTo("Rooms", "/a:feed/a:title/text()", xmlString);

    assertXpathExists("/a:feed/a:updated", xmlString);
    assertXpathExists("/a:feed/a:author", xmlString);
    assertXpathExists("/a:feed/a:author/a:name", xmlString);
  }

  private String verifyResponse(final ODataResponse response) throws IOException {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_ATOM_XML_FEED_CS_UTF_8.toContentTypeString(), response.getContentHeader());
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    return xmlString;
  }

  @Test
  public void testInlineCountAllpages() throws Exception {
    initializeRoomData(20);

    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI)
        .mediaResourceMimeType("mediatype")
        .inlineCount(Integer.valueOf(103))
        .inlineCountType(InlineCount.ALLPAGES)
        .build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/m:count", xmlString);
    assertXpathEvaluatesTo("103", "/a:feed/m:count/text()", xmlString);
  }

  @Test
  public void testInlineCountNone() throws Exception {
    when(view.getInlineCount()).thenReturn(InlineCount.NONE);

    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathNotExists("/a:feed/m:count", xmlString);
  }

  @Test
  public void testNextLink() throws Exception {
    when(view.getInlineCount()).thenReturn(InlineCount.NONE);

    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI)
        .mediaResourceMimeType("mediatype")
        .nextLink("http://thisisanextlink")
        .build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/a:link[@rel='next']", xmlString);
    assertXpathEvaluatesTo("http://thisisanextlink", "/a:feed/a:link[@rel='next']/@href", xmlString);
  }

  @Test(expected = EntityProviderException.class)
  public void testInlineCountInvalid() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").inlineCountType(InlineCount.ALLPAGES).build();
    ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
  }

  @Test
  public void testEntries() throws Exception {
    initializeRoomData(103);

    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").build();
    ODataResponse response = ser.writeFeed(view.getTargetEntitySet(), roomsData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:feed/a:entry[1]", xmlString);
    assertXpathExists("/a:feed/a:entry[2]", xmlString);
    assertXpathExists("/a:feed/a:entry[103]", xmlString);
  }

}
