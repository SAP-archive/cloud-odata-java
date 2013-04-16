package com.sap.core.odata.fit.basic;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.testutil.helper.StringHelper;

public class LanguageNegotiationTest extends AbstractBasicTest {

  @SuppressWarnings("unchecked")
  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class)))
        .thenThrow(MyException.class);
    return processor;
  }

  @Override
  @Before
  public void before() {
    super.before();

    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("m", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    disableLogging();
  }

  @Test
  public void issue_ODATAFORSAP_61() throws ClientProtocolException, IOException, XpathException, SAXException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "$metadata"));
    get.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "es");

    HttpResponse response = getHttpClient().execute(get);
    String content = StringHelper.httpEntityToString(response.getEntity());
    assertXpathExists("/m:error/m:message", content);
  }

  @Test
  public void testErrorInItalianLanguage() throws ClientProtocolException, IOException, XpathException, SAXException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "$metadata"));
    get.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "it");

    HttpResponse response = getHttpClient().execute(get);

    String content = StringHelper.httpEntityToString(response.getEntity());

    assertXpathExists("/m:error/m:message", content);
    assertXpathExists("/m:error/m:message[@xml:lang=\"it\"]", content);
    assertXpathEvaluatesTo("itLanguage", "/m:error/m:message/text()", content);

  }

  @Test
  public void testErrorNoLanguage() throws ClientProtocolException, IOException, XpathException, SAXException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "$metadata"));

    HttpResponse response = getHttpClient().execute(get);

    String content = StringHelper.httpEntityToString(response.getEntity());

    assertXpathExists("/m:error/m:message", content);
    assertXpathExists("/m:error/m:message[@xml:lang=\"en\"]", content);
    assertXpathEvaluatesTo("fallbackLanguage", "/m:error/m:message/text()", content);
  }

  static class MyException extends ODataMessageException {

    public MyException(final MessageReference messageReference) {
      super(TEST);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public MessageReference getMessageReference() {
      return TEST;
    }

    public static final MessageReference TEST = createMessageReference(MyException.class, "TEST");
  }

}
