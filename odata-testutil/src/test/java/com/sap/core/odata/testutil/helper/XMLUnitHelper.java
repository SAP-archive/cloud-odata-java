package com.sap.core.odata.testutil.helper;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

/**
 * Helper for XML unit tests.
 * @author SAP AG
 */
public class XMLUnitHelper {

  /**
   * Verifies order of <code>tags</code> in given <code>xmlString</code>.
   * @param xmlString
   * @param toCheckTags
   */
  public static void verifyTagOrdering(String xmlString, String... toCheckTags) {
    int lastTagPos = -1;

    for (final String tagName : toCheckTags) {
      final Pattern p = Pattern.compile(tagName);
      final Matcher m = p.matcher(xmlString);

      if (m.find()) {
        final int currentTagPos = m.start();
        Assert.assertTrue("Tag with name '" + tagName + "' is not in correct order. Expected order is '" + Arrays.toString(toCheckTags) + "'.",
            lastTagPos < currentTagPos);
        lastTagPos = currentTagPos;
      } else {
        Assert.fail("Expected tag '" + tagName + "' was not found in input [\n\n" + xmlString + "\n\n].");
      }

    }
  }
}
