package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.format.SingleLink;
import org.odata4j.format.SingleLinks;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;

public class JsonSingleLinkFormatParser extends JsonFormatParser implements FormatParser<SingleLink> {

  public JsonSingleLinkFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public SingleLink parse(Reader reader) {
    // {"uri": "http://host/service.svc/Orders(1)"}
    JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    try {
      ensureStartObject(jsr.nextEvent());
      ensureStartProperty(jsr.nextEvent(), "uri");
      String uri = jsr.nextEvent().asEndProperty().getValue();
      return SingleLinks.create(uri);
    } finally {
      jsr.close();
    }
  }

}
