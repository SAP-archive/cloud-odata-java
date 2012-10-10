package org.odata4j.stax2.util;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Enumerable;
import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.QName2;

public class InMemoryAttributes {

  private final List<Attribute2> attributes = new ArrayList<Attribute2>();

  public Enumerable<Attribute2> getAttributes() {
    return Enumerable.create(attributes);
  }

  public Attribute2 getAttributeByName(String name) {
    return getAttributes().firstOrNull(InMemoryAttribute2.pred1_byName(name));
  }

  public Attribute2 getAttributeByName(QName2 name) {
    return getAttributes().firstOrNull(InMemoryAttribute2.pred1_byQName(name));
  }

  public void put(String namespaceUri, String name, String prefix, String value) {
    QName2 qname = new QName2(namespaceUri, name, prefix);
    Attribute2 attribute = new InMemoryAttribute2(qname, value);
    attributes.add(attribute);
  }

}
