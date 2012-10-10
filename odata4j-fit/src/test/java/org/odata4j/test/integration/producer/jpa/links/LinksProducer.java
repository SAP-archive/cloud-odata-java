package org.odata4j.test.integration.producer.jpa.links;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Func;
import org.odata4j.producer.inmemory.InMemoryProducer;

public class LinksProducer extends InMemoryProducer {

  public LinksProducer() {
    super("LinksProducer");

    this.register(A.class, "As", new Func<Iterable<A>>() {

      @Override
      public Iterable<A> apply() {
        return as;
      }

    }, "Name");

    this.register(B.class, "Bs", new Func<Iterable<B>>() {

      @Override
      public Iterable<B> apply() {
        return bs;
      }

    }, "Name");

    this.register(C.class, "Cs", new Func<Iterable<C>>() {

      @Override
      public Iterable<C> apply() {
        return cs;
      }

    }, "Name");
  }

  //  public static void main(String[] args) {
  //
  //    String endpointUri = "http://localhost:8887/LinksProducer.svc/";
  //
  //    final InMemoryProducer producer = new LinksProducer();
  //
  //    // register the producer as the static instance, then launch the http server
  //    DefaultODataProducerProvider.setInstance(producer);
  //    JerseyProducerUtil.hostODataServer(endpointUri);
  //  }

  private static final List<C> cs = new ArrayList<C>(2);
  private static final List<B> bs = new ArrayList<B>(1);
  private static final List<A> as = new ArrayList<A>(2);

  static {

    cs.add(new C("c0", "c0"));
    cs.add(new C("c1", "c1"));

    bs.add(new B("b0", "b0"));

    A emptyA = new A("a0", "an A with empty links");
    as.add(emptyA);

    A a = new A("a1", "an A with links");
    a.setMyB(bs.get(0));
    a.addC(cs.get(0));
    a.addC(cs.get(1));
    as.add(a);
  }

  // I'm feeling boring today....
  // A --------0..1 -----------B
  // |
  // ----------0..* -----------C

  public static class A {
    private String name;
    private String description;
    private B myB = null;
    private List<C> myCs = new ArrayList<C>();

    public A(String name, String description) {
      this.name = name;
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public String getDescripion() {
      return description;
    }

    public B getMyB() {
      return myB;
    }

    public void setMyB(B value) {
      myB = value;
    }

    public List<C> getMyCs() {
      return myCs;
    }

    public void setMyCs(List<C> value) {
      myCs = value;
    }

    public void addC(C value) {
      myCs.add(value);
    }
  }

  public static class B {
    private String name;
    private String description;

    public B(String name, String description) {
      this.name = name;
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public String getDescripion() {
      return description;
    }
  }

  public static class C {
    private String name;
    private String description;

    public C(String name, String description) {
      this.name = name;
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public String getDescripion() {
      return description;
    }
  }

}
