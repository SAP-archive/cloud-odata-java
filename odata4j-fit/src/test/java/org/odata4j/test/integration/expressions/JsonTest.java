package org.odata4j.test.integration.expressions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Funcs;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.Guid;
import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.core.ORelatedEntityLinkInline;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OStructuralObject;
import org.odata4j.core.UnsignedByte;
import org.odata4j.format.FormatType;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;
import org.odata4j.test.integration.expressions.PojoWithAllTypesComplex.Complex1;
import org.odata4j.test.integration.expressions.PojoWithAllTypesComplex.Complex2;
import org.odata4j.test.integration.expressions.PojoWithAllTypesComplex.Entity1;

public class JsonTest extends AbstractRuntimeTest {

  public static final String uri = "http://localhost:18890/TestService.svc/";

  public JsonTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void testJsonEntity() throws Exception {

    try {
      setup();
      // did the properties round trip ok?
      OEntity e = consumer.getEntity("Pojo", (int) 1).expand("FavoriteEntity,OnNoticeEntities").execute();
      assertPojoEqualsOEntity(pojo, e, e.getProperties());

      PojoWithAllTypesComplex rpojo = producer.toPojo(e, PojoWithAllTypesComplex.class);
      assertPojoEqualsOEntity(rpojo, e, e.getProperties());
      Assert.assertTrue(rpojo.beforeUnmarshalCalled);
      Assert.assertTrue(rpojo.afterUnmarshalCalled);
    } finally {
      server.stop();
    }

  }

  @Test
  public void testGetNavProps() throws Exception {

    try {
      setup();
      // a to-1 nav prop
      OEntity e = consumer.getEntity("Pojo", (int) 1).nav("FavoriteEntity").execute();
      assertNDGT(e);

      // a to-many nav prop
      Enumerable<OEntity> es = consumer.getEntities("Pojo").nav((int) 1, "OnNoticeEntities").execute();
      assertOnNoticeEntities(es.toList());
    } finally {
      server.stop();
    }

  }

  @Test
  public void testSelect() throws Exception {
    // $select now supported in InMemoryProducer.
    try {
      setup();
      String output = this.rtFacade.getWebResource(uri + "Pojo(1)?$format=json&$select=ComplexType,StringList,FavoriteEntity,FavoriteEntity/Prop2&$expand=FavoriteEntity").getEntity();
      System.out.println(output);
      // did the properties round trip ok?
      OEntity e = consumer.getEntity("Pojo", (int) 1).expand("FavoriteEntity").select("ComplexType,StringList,FavoriteEntity,FavoriteEntity/Prop2").execute();

      Assert.assertTrue(e.getProperties().size() == 2);
      Assert.assertNotNull(getPropertyValue("ComplexType", e.getProperties()));
      Assert.assertNotNull(getPropertyValue("StringList", e.getProperties()));

      OEntity ndgt = ((OEntity) e).getLink("FavoriteEntity", ORelatedEntityLinkInline.class).getRelatedEntity();
      Assert.assertTrue(ndgt.getProperties().size() == 1);
      Assert.assertEquals(getPropertyValue("Prop2", ndgt.getProperties()), 1);

    } finally {
      server.stop();
    }

  }

  private PojoWithAllTypesComplex pojo;
  private InMemoryProducer producer;
  private ODataServer server;
  private ODataConsumer consumer;

  private static List<Entity1> getOnNoticeEntities() {
    List<Entity1> l = new ArrayList<Entity1>();

    l.add(new Entity1("Bears", 2));
    l.add(new Entity1("Irony", 33));
    return l;
  }

  private static Entity1 getNDGT() {
    return new Entity1("Neil DeGrasse-Tyson", 1);
  }

  private void setup() throws Exception {

    producer = new InMemoryProducer("JsonTest");
    DefaultODataProducerProvider.setInstance(producer);

    server = this.rtFacade.startODataServer(uri);

    consumer = this.rtFacade.createODataConsumer(uri, FormatType.JSON);
    Assert.assertEquals(0, consumer.getEntitySets().count());

    // register a complex type:
    producer.registerComplexType(PojoWithAllTypes.class, "PojoWithAllTypes");
    producer.registerComplexType(PojoWithAllTypesComplex.Complex2.class, "Complex2");
    producer.registerComplexType(PojoWithAllTypesComplex.Complex1.class, "Complex1");

    producer.register(PojoWithAllTypesComplex.Entity1.class, "Relations", new Func<Iterable<Entity1>>() {

      @Override
      public Iterable<Entity1> apply() {
        List<Entity1> l = getOnNoticeEntities();
        l.add(getNDGT());
        return l;
      }
    }, "Prop1");

    List<PojoWithAllTypesComplex> pojos = new ArrayList<PojoWithAllTypesComplex>();
    producer.register(PojoWithAllTypesComplex.class, "Pojo", Funcs.constant((Iterable<PojoWithAllTypesComplex>) pojos), "Int32");

    List<String> stringList = new ArrayList<String>();
    stringList.add("tag1");
    stringList.add("tag2");
    stringList.add("tag3");

    PojoWithAllTypes embeddedPojo =
        new PojoWithAllTypes(new byte[] { 0x04, 0x05, 0x06 }, false, UnsignedByte.valueOf(0xEE), (byte) -0x04, new LocalDateTime(), new BigDecimal("223.456"), 223.456,
            Guid.randomGuid(), (short) 124, 2, Long.MAX_VALUE - 1, 124.456F, "JohnEmbedded", new LocalTime(), new DateTime());

    pojo =
        new PojoWithAllTypesComplex(new byte[] { 0x01, 0x02, 0x03 }, true, UnsignedByte.valueOf(0xFF), (byte) -0x05, new LocalDateTime(), new BigDecimal("123.456"), 123.456,
            Guid.randomGuid(), (short) 123, 1, Long.MAX_VALUE, 123.456F, "John", new LocalTime(), new DateTime(), stringList, embeddedPojo);
    pojo.addComplex1(new Complex1("c1a", "c1b", new Complex2("c2a", "c2b"), null, null)).addComplex1(new Complex1("c2a", "c2b", null, Arrays.asList(new Complex2[] { new Complex2("cc2a", "cc2b") }), Arrays.asList(new String[] { "es1", "es2" })));
    pojo.setFavoriteEntity(getNDGT());
    pojo.setOnNoticeEntities(getOnNoticeEntities());
    pojos.add(pojo);

    {
      //String output = this.rtFacade.getWebResource(uri + "$metadata");
      //System.out.println(output);
    }
    //String output = this.rtFacade.getWebResource(uri + "Pojo?$format=json&$expand=FavoriteEntity,OnNoticeEntities");
    //System.out.println(output);
  }

  private static Object getPropertyValue(String name, List<OProperty<?>> props) {
    for (OProperty<?> p : props) {
      if (p.getName().equals(name)) {
        return p.getValue();
      }
    }
    return null;
  }

  private static void assertPojoEqualsOEntity(PojoWithAllTypes pojo, OStructuralObject sobject, List<OProperty<?>> props) {
    Assert.assertEquals(pojo.getBoolean(), getPropertyValue("Boolean", props));
    // TODO when Edm.Binary supported by InMemoryProducer
    // assertArrayEquals(pojo.getBinary(), (byte[])getPropertyValue("Binary", props));
    Assert.assertEquals(pojo.getByte(), getPropertyValue("Byte", props));
    Assert.assertEquals(pojo.getDateTime(), getPropertyValue("DateTime", props));
    Assert.assertTrue(pojo.getDecimal().compareTo((BigDecimal) getPropertyValue("Decimal", props)) == 0);
    Assert.assertEquals(pojo.getDouble(), getPropertyValue("Double", props));
    Assert.assertEquals(pojo.getGuid(), getPropertyValue("Guid", props));
    Assert.assertEquals(pojo.getInt16(), getPropertyValue("Int16", props));
    Assert.assertEquals(pojo.getInt32(), getPropertyValue("Int32", props));
    Assert.assertEquals(pojo.getInt64(), getPropertyValue("Int64", props));
    Assert.assertEquals(pojo.getSingle(), getPropertyValue("Single", props));
    Assert.assertEquals(pojo.getString(), getPropertyValue("String", props));
    Assert.assertEquals(pojo.getTime(), getPropertyValue("Time", props));
    Assert.assertTrue(pojo.getDateTimeOffset().isEqual((DateTime) getPropertyValue("DateTimeOffset", props)));

    if (pojo instanceof PojoWithAllTypesComplex) {

      // embedded EdmComplexType
      PojoWithAllTypesComplex pojoC = (PojoWithAllTypesComplex) pojo;
      assertPojoEqualsOEntity(pojoC.getComplexType(), null, (List<OProperty<?>>) getPropertyValue("ComplexType", props));

      // embedded collection(Edm.String)
      OCollection<OSimpleObject<String>> scollection = (OCollection<OSimpleObject<String>>) getPropertyValue("StringList", props);
      Assert.assertEquals(scollection.size(), pojoC.getStringList().size());
      for (String sl : pojoC.getStringList()) {
        boolean found = false;
        for (OSimpleObject<String> so : scollection) {
          if (((String) so.getValue()).equals(sl)) {
            found = true;
            break;
          }
        }
        Assert.assertTrue(found);
      }

      // embedded collection(Edm.ComplexType)
      assertComplexes((OCollection<OComplexObject>) getPropertyValue("Complexes", props), pojoC);

      // TODO need a generalized differ here...
      // inlined entity
      OEntity ndgt = ((OEntity) sobject).getLink("FavoriteEntity", ORelatedEntityLinkInline.class).getRelatedEntity();
      assertNDGT(ndgt);

      List<OEntity> one = ((OEntity) sobject).getLink("OnNoticeEntities", ORelatedEntitiesLinkInline.class).getRelatedEntities();
      assertOnNoticeEntities(one);
    }
  }

  private static void assertNDGT(OEntity ndgt) {
    Assert.assertEquals(getNDGT().getProp1(), getPropertyValue("Prop1", ndgt.getProperties()));
  }

  private static void assertOnNoticeEntities(List<OEntity> one) {
    Assert.assertEquals(2, one.size());
    for (OEntity onNotice : one) {
      boolean found = false;
      for (Entity1 e1 : getOnNoticeEntities()) {
        if (e1.getProp1().equals(getPropertyValue("Prop1", onNotice.getProperties()))
            && getPropertyValue("Prop2", onNotice.getProperties()).equals(e1.getProp2())) {
          found = true;
          break;
        }
      }
      Assert.assertTrue(found);
    }
  }

  private static void assertComplexes(OCollection<OComplexObject> ccollection, PojoWithAllTypesComplex pojoC) {
    Assert.assertEquals(ccollection.size(), pojoC.getComplexes().size());
    for (Complex1 c1 : pojoC.getComplexes()) {
      boolean found = false;
      for (OComplexObject co : ccollection) {
        if (((String) getPropertyValue("S1", co.getProperties())).equals(c1.getS1()) &&
            ((String) getPropertyValue("S2", co.getProperties())).equals(c1.getS2())) {
          found = true;
          break;
        }
      }
      Assert.assertTrue(found);
    }
  }

  private static void assertArrayEquals(byte[] a, byte[] b) {
    Assert.assertEquals(a.length, b.length);
    for (int i = 0; i < a.length; i++) {
      Assert.assertEquals(a[i], b[i]);
    }
  }
}
