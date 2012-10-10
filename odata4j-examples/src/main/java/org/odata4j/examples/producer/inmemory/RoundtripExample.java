package org.odata4j.examples.producer.inmemory;

import static org.odata4j.examples.JaxRsImplementation.JERSEY;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Func;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.examples.AbstractExample;
import org.odata4j.examples.ODataServerFactory;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;

public class RoundtripExample extends AbstractExample {

  public static class Customer {

    private int id;
    private String name;

    public Customer() {}

    public Customer(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public void setId(int id) {
      this.id = id;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return String.format("Customer[Id=%s,Name=%s]", id, name);
    }

  }

  public static void main(String[] args) {
    RoundtripExample example = new RoundtripExample();
    example.run(args);
  }

  private void run(String[] args) {

    // create/start the server
    String endpointUri = "http://localhost:8885/RoundtripExample.svc/";

    InMemoryProducer producer = new InMemoryProducer("RoundtripExample");

    producer.register(Customer.class, "Customers", new Func<Iterable<Customer>>() {
      public Iterable<Customer> apply() {
        List<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer(1, "John"));
        return customers;
      }
    }, "Id");

    DefaultODataProducerProvider.setInstance(producer);
    ODataServer server = new ODataServerFactory(JERSEY).startODataServer(endpointUri);

    // create the client
    ODataConsumer.dump.responseHeaders(true);
    ODataConsumer consumer = ODataConsumers.create(endpointUri);

    reportEntities("Customers", consumer.getEntities("Customers").execute());

    for (Customer customer : consumer.getEntities(Customer.class, "Customers").execute())
      report(customer.toString());

    // stop the server
    server.stop();
  }

}
