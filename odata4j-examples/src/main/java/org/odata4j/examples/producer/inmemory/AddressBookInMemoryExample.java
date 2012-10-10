package org.odata4j.examples.producer.inmemory;

import static org.odata4j.examples.JaxRsImplementation.JERSEY;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Func;
import org.joda.time.LocalDateTime;
import org.odata4j.examples.ODataServerFactory;
import org.odata4j.examples.producer.inmemory.addressbook.Person;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;

public class AddressBookInMemoryExample {

  public static InMemoryProducer createProducer() {
    InMemoryProducer producer = new InMemoryProducer(AddressBookInMemoryExample.class.getName());

    producer.register(Person.class, "Persons", new Func<Iterable<Person>>() {
      public Iterable<Person> apply() {
        List<Person> persons = new ArrayList<Person>();
        persons.add(new Person(1, "Susan Summer", "susan@private-domain.net", new LocalDateTime(1975, 7, 22, 0, 0)));
        persons.add(new Person(2, "Walter Winter", "walter.winter@company.com", new LocalDateTime(1968, 1, 13, 0, 0)));
        persons.add(new Person(3, "Frederic Fall", "ff@some-organisation.org", new LocalDateTime(1983, 10, 31, 0, 0)));
        return persons;
      }
    }, "PersonId");

    return producer;
  }

  public static void main(String[] args) {
    DefaultODataProducerProvider.setInstance(createProducer());
    new ODataServerFactory(JERSEY).hostODataServer("http://localhost:8888/AddressBookInMemoryExample.svc/");
  }
}
