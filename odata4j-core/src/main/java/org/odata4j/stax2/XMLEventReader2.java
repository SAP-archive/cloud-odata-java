package org.odata4j.stax2;

public interface XMLEventReader2 {

  boolean hasNext();

  XMLEvent2 nextEvent();

  String getElementText();

}
