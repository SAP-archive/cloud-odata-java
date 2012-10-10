package org.odata4j.stax2.util;

import org.odata4j.stax2.Characters2;
import org.odata4j.stax2.EndElement2;
import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;

public class InMemoryXMLEvent2 implements XMLEvent2 {

  private final StartElement2 start;
  private final EndElement2 end;
  private final Characters2 characters;

  public InMemoryXMLEvent2(StartElement2 start, EndElement2 end, Characters2 characters) {
    this.start = start;
    this.end = end;
    this.characters = characters;
  }

  @Override
  public boolean isStartElement() {
    return start != null;
  }

  @Override
  public StartElement2 asStartElement() {
    return start;
  }

  @Override
  public boolean isEndElement() {
    return end != null;
  }

  @Override
  public EndElement2 asEndElement() {
    return end;
  }

  @Override
  public boolean isCharacters() {
    return characters != null;
  }

  @Override
  public Characters2 asCharacters() {
    return characters;
  }

}
