package com.sap.core.odata.fit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringStreamHelper {
  public static String inputStreamToString(InputStream in) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    while ((line = bufferedReader.readLine()) != null) {
      stringBuilder.append(line + "\n");
    }
    bufferedReader.close();
    return stringBuilder.toString();
  }

}
