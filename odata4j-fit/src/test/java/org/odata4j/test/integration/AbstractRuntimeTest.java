package org.odata4j.test.integration;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.odata4j.core.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run all JUnit test cases twice. Once for Jersey and once for CXF runtime.
 */
@RunWith(Parameterized.class)
public abstract class AbstractRuntimeTest {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected void log(String key, String value) {
    this.logger.info(String.format("# %-30s : %-50s #", key, value));
  }

  protected void logRule() {
    this.logger.info("#######################################################################################");
  }

  static {
    try { // configure CXF logging
      System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Slf4jLogger");

      // configure log4j
      Properties p = new Properties();
      p.load(AbstractRuntimeTest.class.getResourceAsStream("/log4j.properties"));
      PropertyConfigurator.configure(p);

      // configure JUL
      java.util.logging.LogManager.getLogManager().readConfiguration(AbstractRuntimeTest.class.getResourceAsStream("/logging.properties"));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  /**
   * trace each junit error
   */
  @Rule
  public MethodRule watch = new TestWatchman() {
    @Override
    public void failed(Throwable e, FrameworkMethod method) {
      super.failed(e, method);
      AbstractRuntimeTest.this.logger.error(method.getName(), e);
    }

    @Override
    public void starting(FrameworkMethod method) {
      super.starting(method);

      AbstractRuntimeTest.this.logTestClassContext(AbstractRuntimeTest.this.getClass(), method);
    }
  };

  private <T> void logTestClassContext(Class<T> c, FrameworkMethod method) {
    this.log("test class", c.getSimpleName());
    this.log("test method", method.getName());
    this.logRule();
  }

  private final static String RUNTIME_ENVIRONMENT_PROPERTY = "org.odata4j.jaxrs.runtime";

  protected enum RuntimeFacadeType {
    JERSEY, CXF;

    public static RuntimeFacadeType fromString(String value) {
      RuntimeFacadeType st = JERSEY; // default

      value = value.trim().toUpperCase();
      if ("JERSEY".equals(value)) {
        st = JERSEY;
      } else if ("CXF".equals(value)) {
        st = CXF;
      } else {
        throw new IllegalArgumentException("Wrong value for " + AbstractRuntimeTest.RUNTIME_ENVIRONMENT_PROPERTY + " = " + value + ". Allowed is [JERSEY|CXF]");
      }

      return st;
    }
  }

  protected final RuntimeFacade rtFacade;

  public AbstractRuntimeTest(RuntimeFacadeType type) {
    switch (type) {
    case JERSEY:
      this.rtFacade = new JerseyRuntimeFacade();
      break;
    case CXF:
      this.rtFacade = new CxfRuntimeFacade();
      break;
    default:
      throw new RuntimeException("JAX-RS runtime type not supported: " + type);
    }
    this.logRule();
    this.log("parameterized runtime facade", type.toString());
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {

    /*
       TODO Ideally the test suite runs twice using CXF and Jersey as runtime.
       Unfortunately CXF has issues and throws errors if Jersey did run first. This
       issues are addressed to CXF mailing list: users@cxf.apache.org

       Intermediate wise the runtime is selected from environment variable. Allowed configurations:

       org.odata4j.jaxrs.runtime = jersey
       org.odata4j.jaxrs.runtime = cxf
       org.odata4j.jaxrs.runtime = jersey, cxf // currently not supported by CXF

     */

    Object[][] a;
    String value = System.getProperty(AbstractRuntimeTest.RUNTIME_ENVIRONMENT_PROPERTY);
    if (value == null) {
      a = new Object[][] { { RuntimeFacadeType.JERSEY } }; // default
    }
    else {
      StringTokenizer strt = new StringTokenizer(value, " ,;", false);
      a = new Object[strt.countTokens()][1];
      for (int i = 0; strt.hasMoreTokens(); i++) {
        a[i][0] = RuntimeFacadeType.fromString(strt.nextToken());
      }
    }

    return Arrays.asList(a);
  }

}
