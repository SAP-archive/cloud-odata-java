package org.odata4j.format.xml;

import java.io.Reader;

import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.util.StaxUtil;

public class AtomSimpleObjectFormatParser implements FormatParser<OSimpleObject<?>> {

  private final Settings settings;

  public AtomSimpleObjectFormatParser(Settings settings) {
    this.settings = settings;
  }

  @Override
  public OSimpleObject<?> parse(Reader reader) {
    XMLEventReader2 xmlReader = StaxUtil.newXMLEventReader(reader);
    xmlReader.nextEvent(); // start doc
    xmlReader.nextEvent(); // start element

    String text = xmlReader.getElementText();
    EdmSimpleType<?> type = EdmSimpleType.STRING;
    if (settings != null && settings.parseType != null && settings.parseType.isSimple())
      type = (EdmSimpleType<?>) settings.parseType;
    return OSimpleObjects.parse(type, text);
  }

}
