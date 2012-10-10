package org.odata4j.test.unit.producer.inmemory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
import org.junit.Ignore;
import org.junit.Test;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.producer.inmemory.BeanBasedPropertyModel;
import org.odata4j.producer.inmemory.EnumsAsStringsPropertyModelDelegate;
import org.odata4j.producer.inmemory.InMemoryProducer;

/**
 * Test various aspects of InMemoryEdmGenerator
 *
 * hierarchy:
 * RHS
 * Base
 * ----Sub1
 *     ----Sub1_2
 * ----Sub2
 */
public class InMemoryEdmTest {

  public static class RHS {
    public String getRHSProp1() {
      return "";
    }

    public void setRHSProp1() {}
  }

  public static class Base {

    // key
    public String getBaseProp1() {
      return "";
    }

    public void setBaseProp1() {}

    // base class relationships
    public Collection<RHS> getRHSs() {
      return null;
    }

    public void setRHSs(Collection<RHS> value) {}

    public RHS getRHS() {
      return null;
    }

    public void setRHS(RHS value) {}
  }

  public static class Sub1 extends Base {
    public String getSub1Prop1() {
      return "";
    }

    public void setSub1Prop1() {}
  }

  public static class Sub2 extends Base {
    public String getSub2Prop1() {
      return "";
    }

    public void setSub2Prop1() {}
  }

  public static class Sub1_2 extends Sub1 {
    public String getSub1_2Prop1() {
      return "";
    }

    public void setSub1_2Prop1() {}

    // leaf relationships
    public Collection<Sub2> getSub2s() {
      return null;
    }

    public void setSub2s(Collection<Sub2> value) {}

    public Sub2 getSub2() {
      return null;
    }

    public void setSub2(Sub2 value) {}
  }

  private void register(InMemoryProducer p, Class<? extends Object> clazz, boolean flat, String... keys) {
    p.register(clazz, new EnumsAsStringsPropertyModelDelegate(new BeanBasedPropertyModel(clazz, flat)),
        clazz.getSimpleName() + "s", // set
        clazz.getSimpleName(), // type
        null,
        keys); // keys
  }

  private void assertKeys(List<String> keys, String[] expect) {
    assertEquals(expect.length, keys.size());
    for (String k : expect) {
      assertTrue(keys.contains(k));
    }
  }

  private void assertNavProp(String fromType, EdmMultiplicity fromMult, String toType, EdmMultiplicity toMult, EdmNavigationProperty got) {
    assertEquals(fromType, got.getFromRole().getType().getName());
    assertEquals(fromMult, got.getFromRole().getMultiplicity());
    assertEquals(toType, got.getToRole().getType().getName());
    assertEquals(toMult, got.getToRole().getMultiplicity());
  }

  private void assertProps(Enumerable<EdmProperty> got, String... expected) {
    assertEquals(expected.length, got.count());
    for (final String e : expected) {

      EdmProperty p = got.first(new Predicate1<EdmProperty>() {

        @Override
        public boolean apply(EdmProperty t) {
          return t.getName().equals(e);
        }
      });
      assertEquals(e, p.getName());
    }
  }

  @Test
  public void testHierarchyEdm() {
    InMemoryProducer p = new InMemoryProducer("myns",
        null, // String containerName,
        100, // int maxResults,
        null, // EdmDecorator decorator,
        null, // InMemoryTypeMapping typeMapping,
        false); // boolean flattenEdm);

    register(p, RHS.class, false, "RHSProp1");
    register(p, Base.class, false, "BaseProp1");
    register(p, Sub1.class, false, "BaseProp1");
    register(p, Sub1_2.class, false, "BaseProp1");
    register(p, Sub2.class, false, "BaseProp1");

    EdmDataServices edm = p.getMetadata();
    //EdmxFormatWriter.write(edm, new OutputStreamWriter(System.out));

    EdmEntityType rhs = (EdmEntityType) edm.findEdmEntityType("myns." + RHS.class.getSimpleName());
    assertTrue(rhs != null);
    assertTrue(rhs.getBaseType() == null);
    assertKeys(rhs.getKeys(), new String[] { "RHSProp1" });
    assertEquals(0, rhs.getDeclaredNavigationProperties().count());
    assertEquals(1, rhs.getDeclaredProperties().count());
    assertProps(rhs.getDeclaredProperties(), new String[] { "RHSProp1" });
    assertProps(rhs.getProperties(), new String[] { "RHSProp1" });

    EdmEntityType base = (EdmEntityType) edm.findEdmEntityType("myns." + Base.class.getSimpleName());
    assertTrue(base != null);
    assertTrue(base.getBaseType() == null);
    assertKeys(base.getKeys(), new String[] { "BaseProp1" });
    assertProps(base.getDeclaredProperties(), new String[] { "BaseProp1" });
    assertProps(base.getProperties(), new String[] { "BaseProp1" });
    assertEquals(2, base.getDeclaredNavigationProperties().count());
    assertEquals(2, base.getNavigationProperties().count());
    assertNavProp("Base", EdmMultiplicity.MANY, "RHS", EdmMultiplicity.ONE, base.findDeclaredNavigationProperty("RHS"));
    assertNavProp("Base", EdmMultiplicity.ZERO_TO_ONE, "RHS", EdmMultiplicity.MANY, base.findDeclaredNavigationProperty("RHSs"));

    EdmEntityType sub1 = (EdmEntityType) edm.findEdmEntityType("myns." + Sub1.class.getSimpleName());
    assertTrue(sub1 != null);
    assertEquals(base, sub1.getBaseType());
    assertKeys(sub1.getKeys(), new String[] { "BaseProp1" });
    assertProps(sub1.getDeclaredProperties(), new String[] { "Sub1Prop1" });
    assertProps(sub1.getProperties(), new String[] { "BaseProp1", "Sub1Prop1" });
    assertEquals(0, sub1.getDeclaredNavigationProperties().count());
    assertEquals(2, sub1.getNavigationProperties().count());
    assertNavProp("Base", EdmMultiplicity.MANY, "RHS", EdmMultiplicity.ONE, sub1.findNavigationProperty("RHS"));
    assertNavProp("Base", EdmMultiplicity.ZERO_TO_ONE, "RHS", EdmMultiplicity.MANY, sub1.findNavigationProperty("RHSs"));

    EdmEntityType sub2 = (EdmEntityType) edm.findEdmEntityType("myns." + Sub2.class.getSimpleName());
    assertTrue(sub2 != null);
    assertEquals(base, sub2.getBaseType());
    assertKeys(sub2.getKeys(), new String[] { "BaseProp1" });
    assertProps(sub2.getDeclaredProperties(), new String[] { "Sub2Prop1" });
    assertProps(sub2.getProperties(), new String[] { "BaseProp1", "Sub2Prop1" });
    assertEquals(0, sub2.getDeclaredNavigationProperties().count());
    assertEquals(2, sub2.getNavigationProperties().count());
    assertNavProp("Base", EdmMultiplicity.MANY, "RHS", EdmMultiplicity.ONE, sub2.findNavigationProperty("RHS"));
    assertNavProp("Base", EdmMultiplicity.ZERO_TO_ONE, "RHS", EdmMultiplicity.MANY, sub2.findNavigationProperty("RHSs"));

    EdmEntityType sub1_2 = (EdmEntityType) edm.findEdmEntityType("myns." + Sub1_2.class.getSimpleName());
    assertTrue(sub1_2 != null);
    assertEquals(sub1, sub1_2.getBaseType());
    assertKeys(sub1_2.getKeys(), new String[] { "BaseProp1" });
    assertProps(sub1_2.getDeclaredProperties(), new String[] { "Sub1_2Prop1" });
    assertProps(sub1_2.getProperties(), new String[] { "BaseProp1", "Sub1Prop1", "Sub1_2Prop1" });
    assertEquals(2, sub1_2.getDeclaredNavigationProperties().count());
    assertEquals(4, sub1_2.getNavigationProperties().count());
    assertNavProp("Base", EdmMultiplicity.MANY, "RHS", EdmMultiplicity.ONE, sub1_2.findNavigationProperty("RHS"));
    assertNavProp("Base", EdmMultiplicity.ZERO_TO_ONE, "RHS", EdmMultiplicity.MANY, sub1_2.findNavigationProperty("RHSs"));
    assertNavProp("Sub1_2", EdmMultiplicity.MANY, "Sub2", EdmMultiplicity.ONE, sub1_2.findDeclaredNavigationProperty("Sub2"));
    assertNavProp("Sub1_2", EdmMultiplicity.ZERO_TO_ONE, "Sub2", EdmMultiplicity.MANY, sub1_2.findDeclaredNavigationProperty("Sub2s"));
  }

  @Test
  public void testFlatEdm() {
    InMemoryProducer p = new InMemoryProducer("myns");
    register(p, RHS.class, true, "RHSProp1");
    register(p, Sub1.class, true, "BaseProp1");

    EdmDataServices edm = p.getMetadata();
    // EdmxFormatWriter.write(edm, new OutputStreamWriter(System.out));

    EdmEntityType sub1 = (EdmEntityType) edm.findEdmEntityType("myns." + Sub1.class.getSimpleName());
    assertTrue(sub1 != null);
    assertEquals(null, sub1.getBaseType());
    assertKeys(sub1.getKeys(), new String[] { "BaseProp1" });
    assertProps(sub1.getDeclaredProperties(), new String[] { "BaseProp1", "Sub1Prop1" });
    assertProps(sub1.getProperties(), new String[] { "BaseProp1", "Sub1Prop1" });
    assertEquals(2, sub1.getDeclaredNavigationProperties().count());
    assertEquals(2, sub1.getNavigationProperties().count());
    assertNavProp("Sub1", EdmMultiplicity.MANY, "RHS", EdmMultiplicity.ONE, sub1.findNavigationProperty("RHS"));
    assertNavProp("Sub1", EdmMultiplicity.ZERO_TO_ONE, "RHS", EdmMultiplicity.MANY, sub1.findNavigationProperty("RHSs"));
  }

  @Ignore("this currently fails, should not")
  //@Test
  public void testUniqueAssociationNames() {
    InMemoryProducer p = new InMemoryProducer("myns");

    register(p, RHS.class, true, "RHSProp1");
    register(p, Base.class, true, "BaseProp1");

    EdmDataServices edm = p.getMetadata();
    EdmEntityType base = (EdmEntityType) edm.findEdmEntityType("myns.Base");
    EdmNavigationProperty a1 = base.findNavigationProperty("RHS");
    EdmNavigationProperty a2 = base.findNavigationProperty("RHSs");
    assertFalse(a1.getRelationship().getName() + " should not equal " + a1.getRelationship().getName(),
        a1.getRelationship().getName().equals(a1.getRelationship().getName()));
  }
}
