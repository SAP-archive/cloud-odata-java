package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class JsonErrorDocumentProducer {

  public void writeErrorDocument(final Writer writer, final String errorCode, final String message, final Locale locale, final String innerError) throws IOException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    jsonStreamWriter.beginObject()
        .name(FormatJson.ERROR)
        .beginObject()
        .namedStringValue(FormatJson.CODE, errorCode).separator()
        .name(FormatJson.MESSAGE)
        .beginObject()
        .namedStringValueRaw(FormatJson.LANG,
            locale == null || locale.getLanguage() == null ? null :
                locale.getLanguage() + (locale.getCountry() == null || locale.getCountry().isEmpty() ? "" : ("-" + locale.getCountry())))
        .separator()
        .namedStringValue(FormatJson.VALUE, message)
        .endObject();
    if (innerError != null) {
      jsonStreamWriter.separator()
          .namedStringValue(FormatJson.INNER_ERROR, innerError);
    }
    jsonStreamWriter.endObject()
        .endObject();
  }
}
