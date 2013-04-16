/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
  public static void verifyTagOrdering(final String xmlString, final String... toCheckTags) {
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
