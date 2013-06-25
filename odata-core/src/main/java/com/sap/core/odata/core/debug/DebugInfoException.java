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
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject();
    jsonStreamWriter.name("exceptions");
    if (exception == null) {
      jsonStreamWriter.unquotedValue(null);
    } else {
      Throwable throwable = exception;
      jsonStreamWriter.beginArray();
      while (throwable != null) {
        jsonStreamWriter.beginObject();
        jsonStreamWriter.namedStringValueRaw("class", throwable.getClass().getCanonicalName());
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValue("message",
            throwable instanceof ODataMessageException ?
                MessageService.getMessage(locale, ((ODataMessageException) throwable).getMessageReference()).getText() :
                throwable.getLocalizedMessage());
        jsonStreamWriter.separator();

        jsonStreamWriter.name("invocation");
        jsonStreamWriter.beginObject();
        final StackTraceElement details = throwable.getStackTrace()[0];
        jsonStreamWriter.namedStringValueRaw("class", details.getClassName());
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValueRaw("method", details.getMethodName());
        jsonStreamWriter.separator();
        jsonStreamWriter.name("line");
        jsonStreamWriter.unquotedValue(Integer.toString(details.getLineNumber()));
        jsonStreamWriter.endObject();

        jsonStreamWriter.endObject();
        throwable = throwable.getCause();
        if (throwable != null)
          jsonStreamWriter.separator();
      }
      jsonStreamWriter.endArray();
    }
    jsonStreamWriter.separator();

    jsonStreamWriter.name("stacktrace");
    if (exception == null) {
      jsonStreamWriter.unquotedValue(null);
    } else {
      jsonStreamWriter.beginArray();
      boolean first = true;
      for (final StackTraceElement stackTraceElement : exception.getStackTrace()) {
        if (!first)
          jsonStreamWriter.separator();
        first = false;
        jsonStreamWriter.beginObject();
        jsonStreamWriter.namedStringValueRaw("class", stackTraceElement.getClassName());
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValueRaw("method", stackTraceElement.getMethodName());
        jsonStreamWriter.separator();
        jsonStreamWriter.name("line");
        jsonStreamWriter.unquotedValue(Integer.toString(stackTraceElement.getLineNumber()));
        jsonStreamWriter.endObject();
      }
      jsonStreamWriter.endArray();
    }
    jsonStreamWriter.endObject();
  }
}
