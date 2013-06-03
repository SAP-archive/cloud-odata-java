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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractXmlProducerTestHelper;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class XmlErrorProducerTest extends AbstractXmlProducerTestHelper {

  private static final String contentType = ContentType.APPLICATION_XML.toContentTypeString();
  private static final HttpStatusCodes expectedStatus = HttpStatusCodes.INTERNAL_SERVER_ERROR;

  public XmlErrorProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @BeforeClass
  public static void setup() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Test
  public void viaRuntimeDelegate() throws Exception {
    ODataErrorContext context = new ODataErrorContext();
    context.setContentType(contentType);
    context.setHttpStatus(expectedStatus);
    context.setErrorCode(null);
    context.setMessage(null);
    context.setLocale(null);
    context.setInnerError(null);
    ODataResponse response = EntityProvider.writeErrorDocument(context);
    String errorXml = verifyResponse(response);
    verifyXml(null, null, null, null, errorXml);

    context.setErrorCode("a");
    context.setMessage("a");
    context.setLocale(Locale.GERMAN);
    context.setInnerError("a");
    response = EntityProvider.writeErrorDocument(context);
    errorXml = verifyResponse(response);
    verifyXml("a", "a", Locale.GERMAN, "a", errorXml);

    context.setErrorCode(null);
    context.setInnerError(null);
    response = EntityProvider.writeErrorDocument(context);
    errorXml = verifyResponse(response);
    verifyXml(null, "a", Locale.GERMAN, null, errorXml);
  }

  @Test
  public void viaProviderFacadeImpl() throws Exception {
    String errorCode = null;
    String message = null;
    Locale locale = null;
    String innerError = null;

    ODataErrorContext ctx = new ODataErrorContext();
    ctx.setContentType(contentType);
    ctx.setErrorCode(errorCode);
    ctx.setHttpStatus(expectedStatus);
    ctx.setLocale(locale);
    ctx.setMessage(message);

    ODataResponse response = new ProviderFacadeImpl().writeErrorDocument(ctx);
    String errorXml = verifyResponse(response);
    verifyXml(errorCode, message, locale, innerError, errorXml);

    errorCode = "a";
    message = "a";
    locale = Locale.GERMAN;
    innerError = "a";

    ctx = new ODataErrorContext();
    ctx.setContentType(contentType);
    ctx.setErrorCode(errorCode);
    ctx.setHttpStatus(expectedStatus);
    ctx.setLocale(locale);
    ctx.setMessage(message);
    ctx.setInnerError(innerError);

    response = new ProviderFacadeImpl().writeErrorDocument(ctx);
    errorXml = verifyResponse(response);
    verifyXml(errorCode, message, locale, innerError, errorXml);

    errorCode = null;
    message = "a";
    locale = Locale.GERMAN;
    innerError = null;

    ctx = new ODataErrorContext();
    ctx.setContentType(contentType);
    ctx.setErrorCode(errorCode);
    ctx.setHttpStatus(expectedStatus);
    ctx.setLocale(locale);
    ctx.setMessage(message);
    ctx.setInnerError(innerError);

    response = new ProviderFacadeImpl().writeErrorDocument(ctx);
    errorXml = verifyResponse(response);
    verifyXml(errorCode, message, locale, innerError, errorXml);
  }

  @Test
  public void normal() throws Exception {
    serializeError(null, "Message", null, Locale.GERMAN);
    serializeError(null, "Message", null, Locale.ENGLISH);
    serializeError(null, "Message", null, Locale.CANADA);
    serializeError(null, "Message", null, Locale.FRANCE);
    serializeError(null, "Message", null, Locale.CHINA);
  }

  @Test
  public void none() throws Exception {
    serializeError(null, null, null, null);
  }

  @Test
  public void onlyErrorCode() throws Exception {
    serializeError("ErrorCode", null, null, null);
  }

  @Test
  public void onlyMessage() throws Exception {
    serializeError(null, "message", null, null);
  }

  @Test
  public void onlyInnerError() throws Exception {
    serializeError(null, null, "InnerError", null);
  }

  @Test
  public void onlyLocale() throws Exception {
    serializeError(null, null, null, Locale.GERMANY);
  }

  @Test
  public void withoutMessage() throws Exception {
    serializeError("ErrorCode", null, null, Locale.GERMAN);
  }

  @Test
  public void normalWithErrorCodeVariation() throws Exception {
    serializeError("", "Message", null, Locale.GERMAN);
    serializeError("  ", "Message", null, Locale.GERMAN);
  }

  @Test
  public void normalWithInnerErrorVariation() throws Exception {
    serializeError(null, "Message", "", Locale.GERMAN);
    serializeError(null, "Message", "  ", Locale.GERMAN);
  }

  @Test
  public void all() throws Exception {
    serializeError("ErrorCode", "Message", "InnerError", Locale.GERMAN);
    serializeError("ErrorCode", "Message", "InnerError", Locale.ENGLISH);
    serializeError("ErrorCode", "Message", "InnerError", Locale.CANADA);
    serializeError("ErrorCode", "Message", "InnerError", Locale.FRANCE);
    serializeError("ErrorCode", "Message", "InnerError", Locale.CHINA);
  }

  private String getLang(final Locale locale) {
    if (locale == null) {
      return "";
    }
    if (locale.getCountry().isEmpty()) {
      return locale.getLanguage();
    } else {
      return locale.getLanguage() + "-" + locale.getCountry();
    }
  }

  private void serializeError(final String errorCode, final String message, final String innerError, final Locale locale) throws Exception {
    ODataResponse response = new AtomEntityProvider().writeErrorDocument(expectedStatus, errorCode, message, locale, innerError);
    String errorXml = verifyResponse(response);
    verifyXml(errorCode, message, locale, innerError, errorXml);
  }

  private String verifyResponse(final ODataResponse response) throws IOException {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toContentTypeString(), response.getContentHeader());
    assertEquals(expectedStatus, response.getStatus());
    assertNotNull(response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    return xmlString;
  }

  private void verifyXml(final String errorCode, final String message, final Locale locale, final String innerError, final String errorXml) throws Exception {

    assertXpathExists("/a:error", errorXml);

    if (errorCode != null) {
      assertXpathEvaluatesTo(errorCode, "/a:error/a:code", errorXml);
    } else {
      assertXpathExists("/a:error/a:code", errorXml);
    }
    if (message != null) {
      assertXpathEvaluatesTo(message, "/a:error/a:message", errorXml);
      assertXpathExists("/a:error/a:message[@xml:lang=\"" + getLang(locale) + "\"]", errorXml);
    } else {
      if (locale == null) {
        assertXpathExists("/a:error/a:message[@xml:lang='']", errorXml);
      } else {
        assertXpathExists("/a:error/a:message[@xml:lang=\"" + getLang(locale) + "\"]", errorXml);
      }
    }

    if (innerError == null) {
      assertXpathNotExists("/a:error/a:innererror", errorXml);
    } else {
      assertXpathExists("/a:error/a:innererror", errorXml);
    }
  }
}
