package com.sap.core.odata.fit.basic;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathValuesEqual;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Matchers;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.testutil.helper.ClassHelper;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
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
    when(processor.readEntity(any(GetEntityUriInfo.class), any(String.class))).thenThrow(new ODataNotFoundException(ODataNotFoundException.ENTITY));

    HttpResponse response = executeGetRequest("Managers('199')");
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());

    String content = StringHelper.inputStreamToString(response.getEntity().getContent());
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);
    assertXpathValuesEqual("\"" + ODataNotFoundException.ENTITY.getKey() + "\"", "/a:error/a:code", content);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", content);
  }

  @Test
  public void testGenericHttpExceptions() throws Exception {
    disableLogging();

    List<ODataHttpException> toTestExceptions = getHttpExceptionsForTest();

    int firstKey = 1;
    for (ODataHttpException oDataException : toTestExceptions) {
      String key = String.valueOf(firstKey++);
      Matcher<GetEntityUriInfo> match = new EntityKeyMatcher(key);
      when(processor.readEntity(Matchers.argThat(match), any(String.class))).thenThrow(oDataException);

      HttpResponse response = executeGetRequest("Managers('" + key + "')");

      assertEquals("Expected status code does not match for exception type '" + oDataException.getClass().getSimpleName() + "'.",
          oDataException.getHttpStatus().getStatusCode(), response.getStatusLine().getStatusCode());

      String content = StringHelper.inputStreamToString(response.getEntity().getContent());
      Map<String, String> prefixMap = new HashMap<String, String>();
      prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
      NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
      XMLUnit.setXpathNamespaceContext(ctx);
      assertXpathValuesEqual("\"com.sap.core.odata.api.exception.ODataHttpException.SIMPLE FOR TEST\"", "/a:error/a:code", content);

      //TODO: How to check for the right text here?
      //assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", content);
    }

  }

  private List<ODataHttpException> getHttpExceptionsForTest() throws Exception {
    List<Class<ODataHttpException>> exClasses = ClassHelper.getAssignableClasses("com.sap.core.odata.api.exception", ODataHttpException.class);

    MessageReference mr = MessageReference.create(ODataHttpException.class, "SIMPLE FOR TEST");
    return ClassHelper.getClassInstances(exClasses, new Class<?>[] { MessageReference.class }, new Object[] { mr });
  }

  /**
   * 
   */
  private class EntityKeyMatcher extends BaseMatcher<GetEntityUriInfo> {

    private final String keyLiteral;

    public EntityKeyMatcher(String keyLiteral) {
      if (keyLiteral == null) {
        throw new IllegalArgumentException("Key parameter MUST NOT be NULL.");
      }
      this.keyLiteral = keyLiteral;
    }

    @Override
    public boolean matches(Object item) {
      if (item instanceof UriInfoImpl) {
        UriInfoImpl upr = (UriInfoImpl) item;
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
}
