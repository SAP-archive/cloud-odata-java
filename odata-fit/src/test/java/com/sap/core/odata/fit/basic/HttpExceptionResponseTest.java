package com.sap.core.odata.fit.basic;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathValuesEqual;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Matchers;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.uri.UriParserResultImpl;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.testutils.helper.ClassHelper;
import com.sap.core.odata.testutils.helper.StringHelper;

public class HttpExceptionResponseTest extends AbstractBasicTest {

  private ODataSingleProcessor processor;

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    processor = mock(ODataSingleProcessor.class);

    return processor;
  }

  @Override
  EdmProvider createEdmProvider() {
    EdmProvider provider = new ScenarioEdmProvider();
    return provider;
  }

  @Test
  public void test404HttpNotFound() throws Exception {
    when(processor.readEntity(any(GetEntityView.class),any(ContentType.class))).thenThrow(new ODataNotFoundException(ODataNotFoundException.ENTITY));

    HttpResponse response = executeGetRequest("Managers('199')");
    assertEquals(404, response.getStatusLine().getStatusCode());

    String content = StringHelper.inputStreamToString(response.getEntity().getContent());
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);
    assertXpathValuesEqual("\"" + ODataNotFoundException.ENTITY.getKey() + "\"", "/a:error/a:code", content);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", content);
  }

  @Test
  public void testGenericHttpExceptions() throws Exception {

    List<ODataHttpException> toTestExceptions = getHttpExceptionsForTest();

    int firstKey = 1;
    for (ODataHttpException oDataException : toTestExceptions) {
      String key = String.valueOf(firstKey++);
      Matcher<GetEntityView> match = new EntityKeyMatcher(key);
      when(processor.readEntity(Matchers.argThat(match),any(ContentType.class))).thenThrow(oDataException);

      String uri = getEndpoint().toString() + "Managers('" + key + "')";
      HttpGet get = new HttpGet(URI.create(uri));
      HttpResponse response = getHttpClient().execute(get);

      assertEquals("Expected status code does not match for exception type '" + oDataException.getClass().getSimpleName() + "'.",
          oDataException.getHttpStatus().getStatusCode(), response.getStatusLine().getStatusCode());

      String content = StringHelper.inputStreamToString(response.getEntity().getContent());
      Map<String, String> prefixMap = new HashMap<String, String>();
      prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
      NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
      XMLUnit.setXpathNamespaceContext(ctx);
      assertXpathValuesEqual("\"com.sap.core.odata.api.exception.ODataHttpException.SIMPLE FOR TEST\"", "/a:error/a:code", content);

      //TODO: How to check for the right text here?
      //assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", content);
      get.releaseConnection();
    }

  }

  private List<ODataHttpException> getHttpExceptionsForTest() throws Exception {
    List<Class<ODataHttpException>> exClasses = ClassHelper.getAssignableClasses("com.sap.core.odata.api.exception", ODataHttpException.class);
    //    log.debug("Found exception classes: " + exClasses.toString());

    MessageReference mr = MessageReference.create(ODataHttpException.class, "SIMPLE FOR TEST");
    return ClassHelper.getClassInstances(exClasses, new Class<?>[] { MessageReference.class }, new Object[] { mr });
  }

  /**
   * 
   */
  private class EntityKeyMatcher extends BaseMatcher<GetEntityView> {

    private final String keyLiteral;

    public EntityKeyMatcher(String keyLiteral) {
      if (keyLiteral == null) {
        throw new IllegalArgumentException("Key parameter MUST NOT be NULL.");
      }
      this.keyLiteral = keyLiteral;
    }

    @Override
    public boolean matches(Object item) {
      if (item instanceof UriParserResultImpl) {
        UriParserResultImpl upr = (UriParserResultImpl) item;
        List<KeyPredicate> keyPredicates = upr.getKeyPredicates();
        for (KeyPredicate keyPredicate : keyPredicates) {
          if (keyLiteral.equals(keyPredicate.getLiteral())) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public void describeTo(Description description) {
      //      description.appendText("");
    }

  }

  /**
   * 
   * @param request
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  private HttpResponse executeGetRequest(String request) throws ClientProtocolException, IOException {
    String uri = getEndpoint().toString() + request;
    log.debug("Execute get request for uri '" + uri + "'");

    HttpGet get = new HttpGet(URI.create(uri));
    return getHttpClient().execute(get);
  }
}
