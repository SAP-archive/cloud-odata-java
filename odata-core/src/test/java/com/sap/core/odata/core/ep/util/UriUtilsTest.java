package com.sap.core.odata.core.ep.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class UriUtilsTest {

  @Test
  public void simpleUri() throws Exception {
    String inputUri = "http://localhost:8080";
    
    String result = UriUtils.encodeUri(inputUri);
    
    assertEquals("http://localhost:8080", result);
  }

  @Test
  public void uriWithExtension() throws Exception {
    String inputUri = "http://localhost:8080/FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='00 17',carrid='AA')";
    
    String result = UriUtils.encodeUri(inputUri);
    
    assertEquals("http://localhost:8080/FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='00%2017',carrid='AA')", result);
  }
  
  @Test
  @Ignore
  public void uriWithout() throws Exception {
    String inputUri = "FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='0017',carrid='AA')";
//    String inputUri = "FlightCollection(fldate='2011-07-19T22 00 00',connid='0017',carrid='AA')";
    
    String result = UriUtils.encodeUri(inputUri);
    
    assertEquals("FlightCollection(fldate=datetime'2011-07-19T22:00:00',connid='0017',carrid='AA')", result);
  }

  @Test
  public void relativeUriWithout() throws Exception {
    String inputUri = "Flight:Collection(balasd='test',next='bla-1')";
    
    String result = UriUtils.encodeUri(inputUri);
    
    assertEquals("Flight:Collection(balasd='test',next='bla-1')", result);
  }
}
