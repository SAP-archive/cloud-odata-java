package com.sap.core.odata.core.ep.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UriUtilsTest {

  @Test
  public void uriWithSlashAndDot() throws Exception {
    String inputUri = "./FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='0017',carrid='AA')";

    String result = UriUtils.encodeUriPath(inputUri);

    assertEquals("./FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='0017',carrid='AA')", result);
  }

  @Test
  public void relativeUriWithoutSlash() throws Exception {
    String inputUri = "Flight:Collection(balasd='test',next='bla-1')";

    String result = UriUtils.encodeUriPath(inputUri);

    assertEquals("Flight:Collection(balasd='test',next='bla-1')", result);
  }
}
