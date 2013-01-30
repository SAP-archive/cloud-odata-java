package com.sap.core.odata.testutil.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;

/**
 * @author SAP AG
 */
public class StringHelper {
  public static String inputStreamToString(InputStream in) throws IOException {
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
    final StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    while ((line = bufferedReader.readLine()) != null)
      stringBuilder.append(line);

    bufferedReader.close();

    final String result = stringBuilder.toString();

    return result;
  }

  public static String httpEntityToString(HttpEntity entity) throws IOException, IllegalStateException {
    return inputStreamToString(entity.getContent());
  }
}
