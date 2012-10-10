package org.odata4j.stax2;

import org.core4j.Enumerable;

public interface StartElement2 {

  QName2 getName();

  Attribute2 getAttributeByName(QName2 name);

  Attribute2 getAttributeByName(String name);

  Enumerable<Attribute2> getAttributes();

  Enumerable<Namespace2> getNamespaces();
}
