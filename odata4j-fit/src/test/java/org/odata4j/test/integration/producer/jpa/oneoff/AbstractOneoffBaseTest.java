package org.odata4j.test.integration.producer.jpa.oneoff;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.odata4j.examples.producer.jpa.JPAProvider;
import org.odata4j.producer.jpa.JPAProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;

@Ignore
public class AbstractOneoffBaseTest extends AbstractRuntimeTest {

  public AbstractOneoffBaseTest(RuntimeFacadeType type) {
    super(type);
  }

  protected static String endpointUri;

  protected static EntityManagerFactory emf;
  protected static ODataServer server;

  @After
  public void tearDown() throws Exception {
    if (server != null) {
      server.stop();
    }

    if (emf != null) {
      emf.close();
    }
  }

  @Before
  public void setUp() throws Exception {
    setUp(this.getClass(), 20);
  }

  private void setUp(Class<?> testClass, int maxResults) throws Exception {

    String name = testClass.getSimpleName().split("_")[0];

    endpointUri = "http://localhost:8810/" + name + ".svc/";
    String persistenceUnitName = name + JPAProvider.JPA_PROVIDER.caption;
    String namespace = name;

    Map<String, String> p = new HashMap<String, String>();
    if (JPAProvider.JPA_PROVIDER == JPAProvider.ECLIPSELINK) {
      p.put("eclipselink.target-database", "HSQL");
      p.put("eclipselink.jdbc.driver", "org.hsqldb.jdbcDriver");
      p.put("eclipselink.jdbc.url", "jdbc:hsqldb:mem:" + name + ";shutdown=true;ifexists=false");
      p.put("eclipselink.jdbc.user", "sa");
      p.put("eclipselink.jdbc.password", "");
      p.put("eclipselink.ddl-generation", "create-tables");
      p.put("eclipselink.logging.level", "SEVERE");
      p.put("eclipselink.logging.exceptions", "true");
    } else if (JPAProvider.JPA_PROVIDER == JPAProvider.HIBERNATE) {
      p.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
      p.put("javax.persistence.jdbc.user", "sa");
      p.put("javax.persistence.jdbc.password", "");
      p.put("javax.persistence.jdbc.url", "jdbc:hsqldb:mem:" + name + ";shutdown=true;ifexists=false");
      p.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
      p.put("hibernate.max_fetch_depth", "3");
      p.put("hibernate.hbm2ddl.auto", "create-drop");
    } else {
      throw new UnsupportedOperationException("Implement " + JPAProvider.JPA_PROVIDER);
    }

    emf = Persistence.createEntityManagerFactory(persistenceUnitName, p);

    JPAProducer producer = new JPAProducer(
        emf,
        namespace,
        maxResults);

    DefaultODataProducerProvider.setInstance(producer);
    server = this.rtFacade.startODataServer(endpointUri);
  }

}
