package com.sap.core.odata.fit.basic.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.testutils.fit.AbstractFitTest;

public abstract class AbstractBasicTest extends AbstractFitTest {

  private ODataContext context;

  @Override
  protected ODataSingleProcessorService createService() throws ODataException {
    EdmProvider provider = createEdmProvider();
    ODataSingleProcessor processor = this.createProcessor();

    // science fiction (return context after setContext)
    // see http://www.planetgeek.ch/2010/07/20/mockito-answer-vs-return/

    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        AbstractBasicTest.this.context = (ODataContext) invocation.getArguments()[0];
        return null;
      }
    }).when(processor).setContext(any(ODataContext.class));

    when(processor.getContext()).thenAnswer(new Answer<ODataContext>() {
      @Override
      public ODataContext answer(InvocationOnMock invocation) throws Throwable {
        return AbstractBasicTest.this.context;
      }
    });

    return new ODataSingleProcessorService(provider, processor) {};
  }

  EdmProvider createEdmProvider() {
    return mock(EdmProvider.class);
  }

  abstract ODataSingleProcessor createProcessor() throws ODataException;
}
