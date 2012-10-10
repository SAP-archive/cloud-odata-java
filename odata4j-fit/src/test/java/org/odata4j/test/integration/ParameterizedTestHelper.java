package org.odata4j.test.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterizedTestHelper {

  public static List<Object[]> addVariants(List<Object[]> parametersList, Object... variants) {
    List<Object[]> newParametersList = new ArrayList<Object[]>();
    for (Object[] parameters : parametersList) {
      int newParametersLength = parameters.length + 1;
      for (Object variant : variants) {
        Object[] newParameters = Arrays.copyOf(parameters, newParametersLength);
        newParameters[newParametersLength - 1] = variant;
        newParametersList.add(newParameters);
      }
    }
    return newParametersList;
  }
}
