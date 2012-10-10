package org.odata4j.examples.producer.jpa;

import static org.odata4j.examples.JaxRsImplementation.JERSEY;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.odata4j.examples.ODataServerFactory;
import org.odata4j.producer.jpa.JPAProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;

public class AddressBookJpaExample {

  public static JPAProducer createProducer() {
    String persistenceUnitName = "AddressBookService" + JPAProvider.JPA_PROVIDER.caption;
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    String namespace = "AddressBook";
    JPAProducer producer = new JPAProducer(entityManagerFactory, namespace, 50);
    DatabaseUtils.fillDatabase(namespace.toLowerCase(), "/META-INF/addressbook_insert.sql");

    return producer;
  }

  public static void main(String[] args) {
    DefaultODataProducerProvider.setInstance(createProducer());
    new ODataServerFactory(JERSEY).hostODataServer("http://localhost:8888/AddressBookJpaExample.svc/");
  }
}
