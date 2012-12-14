package com.sap.core.odata.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Matchers;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataForbiddenException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.core.uri.UriParserResultImpl;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
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
  public void test404HttpNotFound() throws ClientProtocolException, IOException, ODataException {
    when(processor.readEntity(any(GetEntityView.class))).thenThrow(new ODataNotFoundException(ODataNotFoundException.ENTITY));

    HttpResponse response = executeGetRequest("Managers('199')");
    assertEquals(404, response.getStatusLine().getStatusCode());

    String content = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("Language = 'en', message = 'Requested entity could not be found.'.", content);
  }

  @Test
  public void testGenericHttpExceptions() throws ClientProtocolException, IOException, ODataException {

    List<ODataHttpException> toTestExceptions = new ArrayList<ODataHttpException>();
    toTestExceptions.add(new ODataNotFoundException(ODataNotFoundException.ENTITY));
    toTestExceptions.add(new ODataBadRequestException(ODataBadRequestException.COMMON));
    toTestExceptions.add(new ODataForbiddenException(ODataForbiddenException.COMMON));

    int firstKey = 1;
    for (ODataHttpException oDataException : toTestExceptions) {
      String key = String.valueOf(firstKey++);
      Matcher<GetEntityView> match = new EntityKeyMatcher(key);
      when(processor.readEntity(Matchers.argThat(match))).thenThrow(oDataException);
      
      //
      String uri = getEndpoint().toString() + "Managers('" + key + "')";
      HttpGet get = new HttpGet(URI.create(uri));
      HttpResponse response = getHttpClient().execute(get);
      
      //
      assertEquals("Expected status code does not match for exception type '" + oDataException.getClass().getSimpleName() + "'.", 
          oDataException.getHttpStatus().getStatusCode(), response.getStatusLine().getStatusCode());
      
//      String content = StringHelper.inputStreamToString(response.getEntity().getContent());
//      assertEquals("Language = 'en', message = 'Requested entity could not be found.'.", content);
      
      get.releaseConnection();
    }
    
  }

  /**
   * 
   */
  private class EntityKeyMatcher extends BaseMatcher<GetEntityView> {

    private final String keyLiteral;
    
    public EntityKeyMatcher(String keyLiteral) {
      if(keyLiteral == null) {
        throw new IllegalArgumentException("Key parameter MUST NOT be NULL.");
      }
      this.keyLiteral = keyLiteral;
    }
    
    @Override
    public boolean matches(Object item) {
      if(item instanceof UriParserResultImpl) {
        UriParserResultImpl upr = (UriParserResultImpl) item;
        List<KeyPredicate> keyPredicates = upr.getKeyPredicates();
        for (KeyPredicate keyPredicate : keyPredicates) {
          if(keyLiteral.equals(keyPredicate.getLiteral())) {
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
