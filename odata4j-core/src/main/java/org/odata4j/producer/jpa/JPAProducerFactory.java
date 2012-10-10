package org.odata4j.producer.jpa;

import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.ODataProducerFactory;

public class JPAProducerFactory implements ODataProducerFactory {

  private final Logger log = Logger.getLogger(getClass().getName());

  public static final String PUNAME_PROPNAME = "odata4j.jpa.persistenceUnitName";
  public static final String NAMESPACE_PROPNAME = "odata4j.jpa.edmNamespace";
  public static final String MAX_RESULTS_PROPNAME = "odata4j.jpa.maxResults";

  @Override
  public ODataProducer create(Properties properties) {
    String persistenceUnitName = properties.getProperty(PUNAME_PROPNAME);
    if (persistenceUnitName == null || persistenceUnitName.length() == 0)
      throw new RuntimeException("Missing required property: " + PUNAME_PROPNAME);

    String edmNamespace = properties.getProperty(NAMESPACE_PROPNAME, "");
    String maxResults = properties.getProperty(MAX_RESULTS_PROPNAME, "50");

    log.info(String.format("Using persistence unit [%s] with edm namespace [%s] and max results [%s]", persistenceUnitName, edmNamespace, maxResults));

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
    JPAProducer producer = new JPAProducer(emf, edmNamespace, Integer.parseInt(maxResults));
    return producer;
  }

}
