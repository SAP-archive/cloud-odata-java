package org.odata4j.stax2.util;

import org.core4j.Predicate1;
import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.QName2;

public class InMemoryAttribute2 implements Attribute2 {

  private final QName2 name;
  private final String value;

  public InMemoryAttribute2(QName2 name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public QName2 getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }

  private static boolean equal(Object lhs, Object rhs) {
    return lhs == rhs || (lhs != null && lhs.equals(rhs));
  }

  public static Predicate1<Attribute2> pred1_byQName(final QName2 name) {
    return new Predicate1<Attribute2>() {
      @Override
      public boolean apply(Attribute2 attribute) {
        return equal(attribute.getName().getNamespaceUri(), name.getNamespaceUri()) && equal(attribute.getName().getLocalPart(), name.getLocalPart());
      }
    };
  }

  public static Predicate1<Attribute2> pred1_byName(final String name) {
    return new Predicate1<Attribute2>() {
      @Override
      public boolean apply(Attribute2 attribute) {
        return attribute.getName().getLocalPart().equals(name);
      }
    };
  }

}