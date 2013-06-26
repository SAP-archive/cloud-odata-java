package com.sap.core.odata.core.debug;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class DebugInfoBody implements DebugInfo {

  private final ODataResponse response;

  public DebugInfoBody(final ODataResponse response) {
    this.response = response;
  }

  @Override
  public String getName() {
    return "Body";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    final String contentType = response.getContentHeader();
    if (contentType == null || response.getEntity() == null) {
      jsonStreamWriter.unquotedValue(null);
    } else if (contentType.startsWith("image/")) {
      jsonStreamWriter.stringValueRaw(Base64.encodeBase64String(getBinaryFromInputStream((InputStream) response.getEntity())));
    } else {
      Object responseObject = response.getEntity();
      String content;
      if (responseObject instanceof String) {
        content = (String) response.getEntity();
      } else if (responseObject instanceof InputStream) {
        content = getStringFromInputStream((InputStream) response.getEntity());
      }
      else {
        throw new ClassCastException("Unsupported content entity class: " + response.getEntity().getClass().getName());
      }

      if (contentType.startsWith(HttpContentType.APPLICATION_JSON)) {
        jsonStreamWriter.unquotedValue(content);
      } else {
        jsonStreamWriter.stringValue(content);
      }
    }
  }

  private static byte[] getBinaryFromInputStream(final InputStream inputStream) {
    if (inputStream == null) {
      return null;
    }

    ByteArrayOutputStream result = new ByteArrayOutputStream();
    int b = -1;
    try {
      while ((b = inputStream.read()) > -1) {
        result.write(b);
      }
      inputStream.close();
    } catch (final IOException e) {
      return null;
    }
    return result.toByteArray();
  }

  private static String getStringFromInputStream(final InputStream inputStream) {
    StringBuilder result = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
      String line = null;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
      reader.close();
      return result.toString();
    } catch (final UnsupportedEncodingException e) {
      return null;
    } catch (final IOException e) {
      return null;
    }
  }
}
