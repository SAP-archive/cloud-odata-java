package com.sap.core.odata.testutils.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;

public class StringHelper {
  public static String inputStreamToString(InputStream in) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    while ((line = bufferedReader.readLine()) != null) {
      stringBuilder.append(line);
    }
    bufferedReader.close();

    String result = stringBuilder.toString();

    return result;
  }

  public static String httpEntityToString(HttpEntity entity) throws UnsupportedEncodingException, IllegalStateException, IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    while ((line = bufferedReader.readLine()) != null)
      stringBuilder.append(line);

    bufferedReader.close();
    return stringBuilder.toString();
  }

}
