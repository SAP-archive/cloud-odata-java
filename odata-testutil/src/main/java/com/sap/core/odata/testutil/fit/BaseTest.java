package com.sap.core.odata.testutil.fit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides basic support for JUnit tests<br>
 * - log & tracing
 * 
 * @author SAP AG
 */
public abstract class BaseTest {

  private static final String CLASSNAME_ODATA_EXCEPTION_MAPPER = "com.sap.core.odata.core.rest.ODataExceptionMapperImpl";

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  private final Map<Class<?>, Level> disabledLoggings = new HashMap<Class<?>, Level>();

  /**
   * trace each junit error
   */
  @Rule
  public TestRule watch = new TestWatcher() {

    @Override
    protected void failed(Throwable e, Description description) {
      super.failed(e, description);
      BaseTest.this.log.error(description.getDisplayName(), e);
    }

    @Override
    protected void starting(Description description) {
      super.starting(description);
      BaseTest.this.log.info("starting " + description.getDisplayName());
    }

    @Override
    protected void finished(Description description) {
      super.finished(description);
      try {} finally {
        reEnableLogging();
      }
    }

    @Override
    protected void succeeded(Description description) {
      super.succeeded(description);
    }

    
    
  };

  /**
   * Disable logging for class with classname {@value #CLASSNAME_ODATA_EXCEPTION_MAPPER}.
   * If no class with this classname can be found an error log entry is written.
   * <br /> 
   * Disabled logging will be automatically re-enabled after test execution (see {@link #reEnableLogging()} and 
   * {@link #after()}).
   * 
   * @param classes
   */
  protected void disableLogging() {
    try {
      disableLogging(Class.forName(CLASSNAME_ODATA_EXCEPTION_MAPPER));
    } catch (ClassNotFoundException e) {
      log.error("Expected class was not found for disabling of logging.");
    }
  }

  /**
   * Disable logging for over handed classes.
   * Disabled logging will be automatically re-enabled after test execution (see {@link #reEnableLogging()} and 
   * {@link #after()}).
   * 
   * @param classes
   */
  protected void disableLogging(Class<?>... classes) {
    for (Class<?> clazz : classes) {
      org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(clazz);
      if (l != null) {
        disabledLoggings.put(clazz, l.getEffectiveLevel());
        l.setLevel(Level.OFF);
      }
    }
  }

  protected void reEnableLogging() {
    for (Entry<Class<?>, Level> entry : disabledLoggings.entrySet()) {
      org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(entry.getKey());
      l.setLevel(entry.getValue());
    }
    disabledLoggings.clear();
  }
}
