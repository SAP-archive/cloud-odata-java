/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.fit.basic;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.fit.AbstractFitTest;

/**
 * @author SAP AG
 */
public abstract class AbstractBasicTest extends AbstractFitTest {

  private ODataContext context;

  @Override
  protected ODataService createService() throws ODataException {
    final EdmProvider provider = createEdmProvider();
    final ODataSingleProcessor processor = createProcessor();

    // science fiction (return context after setContext)
    // see http://www.planetgeek.ch/2010/07/20/mockito-answer-vs-return/

    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(final InvocationOnMock invocation) throws Throwable {
        context = (ODataContext) invocation.getArguments()[0];
        return null;
      }
    }).when(processor).setContext(any(ODataContext.class));

    when(processor.getContext()).thenAnswer(new Answer<ODataContext>() {
      @Override
      public ODataContext answer(final InvocationOnMock invocation) throws Throwable {
        return context;
      }
    });

    return new ODataSingleProcessorService(provider, processor) {};
  }

  EdmProvider createEdmProvider() {
    return mock(EdmProvider.class);
  }

  abstract ODataSingleProcessor createProcessor() throws ODataException;

  protected HttpResponse executeGetRequest(final String request) throws ClientProtocolException, IOException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + request));
    return getHttpClient().execute(get);
  }
}
