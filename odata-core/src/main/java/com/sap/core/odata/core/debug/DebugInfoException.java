package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.util.Locale;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.exception.MessageService;

/**
 * @author SAP AG
 */
public class DebugInfoException implements DebugInfo {

  private final Exception exception;
  private final Locale locale;

  public DebugInfoException(final Exception exception, final Locale locale) {
    this.exception = exception;
    this.locale = locale;
  }

  @Override
  public String getName() {
    return "Stacktrace";
  }

  @Override
  public void appendJson(JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject()
        .name("exceptions")
        .beginArray();
    Throwable throwable = exception;
    while (throwable != null) {
      jsonStreamWriter.beginObject()
          .namedStringValueRaw("class", throwable.getClass().getCanonicalName()).separator()
          .namedStringValue("message",
              throwable instanceof ODataMessageException ?
                  MessageService.getMessage(locale, ((ODataMessageException) throwable).getMessageReference()).getText() :
                  throwable.getLocalizedMessage())
          .separator();

      jsonStreamWriter.name("invocation");
      appendJsonStackTraceElement(jsonStreamWriter, throwable.getStackTrace()[0]);

      jsonStreamWriter.endObject();
      throwable = throwable.getCause();
      if (throwable != null)
        jsonStreamWriter.separator();
    }
    jsonStreamWriter.endArray();
    jsonStreamWriter.separator();

    jsonStreamWriter.name("stacktrace")
        .beginArray();
    boolean first = true;
    for (final StackTraceElement stackTraceElement : exception.getStackTrace()) {
      if (!first)
        jsonStreamWriter.separator();
      first = false;
      appendJsonStackTraceElement(jsonStreamWriter, stackTraceElement);
    }
    jsonStreamWriter.endArray();
    jsonStreamWriter.endObject();
  }

  private static void appendJsonStackTraceElement(JsonStreamWriter jsonStreamWriter, final StackTraceElement stackTraceElement) throws IOException {
    jsonStreamWriter.beginObject()
        .namedStringValueRaw("class", stackTraceElement.getClassName()).separator()
        .namedStringValueRaw("method", stackTraceElement.getMethodName()).separator()
        .name("line").unquotedValue(Integer.toString(stackTraceElement.getLineNumber()))
        .endObject();
  }
}
