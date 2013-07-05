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
package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class PathSegmentTest extends BaseTest {

  PathSegment segment;
  PathSegment segmentNoMatrix;

  @Before
  public void before() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("a", Arrays.asList("1"));
    map.put("b", Arrays.asList("2"));
    map.put("c", Arrays.asList("3"));
    map.put("m", Arrays.asList("x", "y", "z"));

    segment = new ODataPathSegmentImpl("segment", map);
    segmentNoMatrix = new ODataPathSegmentImpl("segment", null);
  }

  @Test
  public void testPathSegement() {
    assertEquals("segment", segment.getPath());

    assertEquals("1", segment.getMatrixParameters().get("a").get(0));
    assertEquals("2", segment.getMatrixParameters().get("b").get(0));
    assertEquals("3", segment.getMatrixParameters().get("c").get(0));

    assertEquals("x", segment.getMatrixParameters().get("m").get(0));
    assertEquals("y", segment.getMatrixParameters().get("m").get(1));
    assertEquals("z", segment.getMatrixParameters().get("m").get(2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void readonlyMatrixParameter() {
    segment.getMatrixParameters().get("m").clear();
  }

  @Test
  public void noMatrixParameter() {
    assertEquals("segment", segmentNoMatrix.getPath());
    assertTrue(segmentNoMatrix.getMatrixParameters().isEmpty());
  }
}
