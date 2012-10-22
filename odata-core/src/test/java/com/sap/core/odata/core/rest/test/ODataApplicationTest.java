package com.sap.core.odata.core.rest.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.junit.Test;
import org.odata4j.producer.resources.ExceptionMappingProvider;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.rest.ODataApplication;
import com.sap.core.odata.core.rest.ODataRootLocator;
import com.sap.core.odata.core.rest.impl.ODataExceptionMapper;

public class ODataApplicationTest {

  private class TestApplication extends ODataApplication {
    @Override
    protected Class<? extends ContextResolver<ODataSingleProcessor>> getContextResolver() {
      return TestContextResolver.class;
    }
  }

  private class TestApplicationNoProvider extends ODataApplication {
    @Override
    protected Class<? extends ContextResolver<ODataSingleProcessor>> getContextResolver() {
      return TestContextResolverNoProvider.class;
    }
  }

  @Provider
  private class TestContextResolver implements ContextResolver<ODataSingleProcessor> {
    @Override
    public ODataSingleProcessor getContext(Class<?> type) {
      return null;
    }
  }

  private class TestContextResolverNoProvider implements ContextResolver<ODataSingleProcessor> {
    @Override
    public ODataSingleProcessor getContext(Class<?> type) {
      return null;
    }
  }
  
  
  @Test
  public void testODataAppliactionClasses() {
    TestApplication app = new TestApplication();
    Set<Class<?>> classes = app.getClasses();

    assertFalse(classes.isEmpty());
  
    assertTrue(classes.contains(TestContextResolver.class));
    assertTrue(classes.contains(ODataRootLocator.class));
    assertTrue(classes.contains(ODataExceptionMapper.class));
  }
  
  @Test(expected=RuntimeException.class)
  public void testNoProviderError() {
    Application app = new TestApplicationNoProvider();
    app.getClasses();
  }
}
