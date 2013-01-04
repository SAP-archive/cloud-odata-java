package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.uri.PathSegment;

public class PathSegmentTest {

  PathSegment segment;
  PathSegment segmentNoMatrix;

  @Before
  public void before() {
    Map<String, List<String>> mp = new HashMap<String, List<String>>();

    List<String> v1 = new ArrayList<String>();
    v1.add("1");
    List<String> v2 = new ArrayList<String>();
    v2.add("2");
    List<String> v3 = new ArrayList<String>();
    v3.add("3");

    List<String> m = new ArrayList<String>();
    m.add("x");
    m.add("y");
    m.add("z");

    mp.put("a", v1);
    mp.put("b", v2);
    mp.put("c", v3);
    mp.put("m", m);

    this.segment = new ODataPathSegmentImpl("segment", mp);
    this.segmentNoMatrix = new ODataPathSegmentImpl("segment", null);
  }

  @Test
  public void testPathSegement() {
    assertEquals("segment", this.segment.getPath());

    assertEquals("1", this.segment.getMatrixParameters().get("a").get(0));
    assertEquals("2", this.segment.getMatrixParameters().get("b").get(0));
    assertEquals("3", this.segment.getMatrixParameters().get("c").get(0));

    assertEquals("x", this.segment.getMatrixParameters().get("m").get(0));
    assertEquals("y", this.segment.getMatrixParameters().get("m").get(1));
    assertEquals("z", this.segment.getMatrixParameters().get("m").get(2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void readonlyMatrixParameter() {
    this.segment.getMatrixParameters().get("m").clear();
  }

  @Test
  public void noMatrixParameter() {
    assertEquals("segment", this.segmentNoMatrix.getPath());
    assertTrue(this.segmentNoMatrix.getMatrixParameters().isEmpty());

  }
}
