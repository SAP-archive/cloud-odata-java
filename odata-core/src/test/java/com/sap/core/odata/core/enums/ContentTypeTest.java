package com.sap.core.odata.core.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.core.enums.ContentType.ODataFormat;

// 14.1 Accept
//
// The Accept request-header field can be used to specify certain media types which are acceptable for the response.
// Accept headers can be used to indicate that the request is specifically limited to a small set of desired types, as
// in the case of a request for an in-line image.
//
// Accept = "Accept" ":"
// #( media-range [ accept-params ] )
// media-range = ( "*/*"
// | ( type "/" "*" )
// | ( type "/" subtype )
// ) *( ";" parameter )
// accept-params = ";" "q" "=" qvalue *( accept-extension )
// accept-extension = ";" token [ "=" ( token | quoted-string ) ]

public class ContentTypeTest {

  @Test
  public void testContentTypeCreation() {
    ContentType mt = ContentType.create("type", "subtype");
    
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals("type/subtype", mt.toString());
    assertEquals(ODataFormat.CUSTOM, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationAtom() {
    ContentType mt = ContentType.create("application", "atom+xml");
    
    assertEquals("application", mt.getType());
    assertEquals("atom+xml", mt.getSubtype());
    assertEquals("application/atom+xml", mt.toString());
    assertEquals(ODataFormat.ATOM, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationXml() {
    ContentType mt = ContentType.create("application", "xml");
    
    assertEquals("application", mt.getType());
    assertEquals("xml", mt.getSubtype());
    assertEquals("application/xml", mt.toString());
    assertEquals(ODataFormat.XML, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationJson() {
    ContentType mt = ContentType.create("application", "json");
    
    assertEquals("application", mt.getType());
    assertEquals("json", mt.getSubtype());
    assertEquals("application/json", mt.toString());
    assertEquals(ODataFormat.JSON, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationOneString() {
    ContentType mt = ContentType.create("type/subtype");
    
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals("type/subtype", mt.toString());
    assertEquals(ODataFormat.CUSTOM, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationAtomOneString() {
    ContentType mt = ContentType.create("application/atom+xml");
    
    assertEquals("application", mt.getType());
    assertEquals("atom+xml", mt.getSubtype());
    assertEquals("application/atom+xml", mt.toString());
    assertEquals(ODataFormat.ATOM, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationXmlOneString() {
    ContentType mt = ContentType.create("application/xml");
    
    assertEquals("application", mt.getType());
    assertEquals("xml", mt.getSubtype());
    assertEquals("application/xml", mt.toString());
    assertEquals(ODataFormat.XML, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationXmlWithParaOneString() {
    ContentType mt = ContentType.create("application/xml;q=0.9");
    
    assertEquals("application", mt.getType());
    assertEquals("xml", mt.getSubtype());
    assertEquals("application/xml", mt.toString());
    assertEquals(ODataFormat.XML, mt.getODataFormat());
  }

  @Test
  public void testContentTypeCreationJsonOneString() {
    ContentType mt = ContentType.create("application/json");
    
    assertEquals("application", mt.getType());
    assertEquals("json", mt.getSubtype());
    assertEquals("application/json", mt.toString());
    assertEquals(ODataFormat.JSON, mt.getODataFormat());
  }

  @Test
  public void testContentTypeWithParameterCreation() {
    ContentType mt = ContentType.create("type", "subtype", addParameters("key", "value"));

    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals(1, mt.getParameters().size());
    assertEquals("value", mt.getParameters().get("key"));
    assertEquals("type/subtype; key=value", mt.toString());
  }

//  private Map<String, String> addParameter(String key, String value) {
//    Map<String, String> map = new HashMap<String, String>();
//    map.put(key, value);
//    return map;
//  }

  private Map<String, String> addParameters(String ... content) {
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < content.length-1; i+=2) {
      String key = content[i];
      String value = content[i+1];
      map.put(key, value);
    }
    return map;
  }

  @Test
  public void testContentTypeWithParametersCreation() {
    ContentType mt = ContentType.create("type", "subtype", addParameters("key1", "value1", "key2", "value2"));
    assertEquals("type", mt.getType());
    assertEquals("subtype", mt.getSubtype());
    assertEquals(2, mt.getParameters().size());
    assertEquals("value1", mt.getParameters().get("key1"));
    assertEquals("value2", mt.getParameters().get("key2"));
    assertEquals("type/subtype; key1=value1; key2=value2", mt.toString());
  }

  @Test
  public void testFormatParserValidInputType() {
    ContentType t = ContentType.create("aaa");

    assertEquals("aaa", t.getType());
    assertEquals("*", t.getSubtype());
    assertEquals(0, t.getParameters().size());
  }

  @Test
  public void testFormatParserValidInputTypeSubtype() {
    ContentType t = ContentType.create("aaa/bbb");
    assertEquals("aaa", t.getType());
    assertEquals("bbb", t.getSubtype());
    assertEquals(0, t.getParameters().size());
  }

  @Test
  public void testFormatParserValidInputTypeSybtypePara() {
    ContentType t = ContentType.create("aaa/bbb;x=y");
    assertEquals("aaa", t.getType());
    assertEquals("bbb", t.getSubtype());
    assertEquals(1, t.getParameters().size());
  }

  @Test
  public void testFormatParserValidInputTypeSubtypeParas() {
    ContentType t = ContentType.create("aaa/bbb;x=y;a=b");
    assertEquals("aaa", t.getType());
    assertEquals("bbb", t.getSubtype());
    assertEquals(2, t.getParameters().size());
  }

  @Test
  public void testFormatParserValidInputTypeSubtypeNullPara() {
    ContentType t = ContentType.create("aaa/bbb;x=y;a");

    assertEquals("aaa", t.getType());
    assertEquals("bbb", t.getSubtype());
    assertEquals(2, t.getParameters().size());
  }

  @Test
  public void testFormatParserValidInputTypeNullPara() {
    ContentType t = ContentType.create("aaa;x=y;a");

    assertEquals("aaa", t.getType());
    assertEquals("*", t.getSubtype());
    assertEquals(2, t.getParameters().size());
  }
  
  @Test
  public void testSimpleEqual() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertEquals(t1, t2);
  }

  @Test
  public void testEqualWithParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testEqualWithUnsortedParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a=b");
    ContentType t2 = ContentType.create("aaa/bbb;a=b;x=y");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testEqualWithWildcard() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("*");
    
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
    assertEquals(t1, t2);
  }

  @Test
  public void testEqualWithWildcardSubtype() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("aaa/*");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testEqualWithDiffTypeWildcardSubtype() {
    ContentType t1 = ContentType.create("ccc/bbb");
    ContentType t2 = ContentType.create("aaa/*");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testEqualWithDiffSubTypeWildcardSubtype() {
    ContentType t1 = ContentType.create("*/bbb");
    ContentType t2 = ContentType.create("aaa/ccc");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testEqualWithWildcardAndParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a");
    ContentType t2 = ContentType.create("*");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testEqualWithWildcardSubtypeAndParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a");
    ContentType t2 = ContentType.create("aaa/*");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }
  
  @Test
  public void testEqualWithWildcardSubtypeAndParametersBoth() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y");
    ContentType t2 = ContentType.create("aaa/*;x=y");
    
    assertEquals(t1, t2);
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  @Ignore("If ContentType contains wildcards parameters are ignored.")
  public void testUnEqualWithWildcardSubtypeAndDiffParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=z");
    ContentType t2 = ContentType.create("aaa/*;x=y");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }
  
  
  @Test
  public void testUnSimpleEqual() {
    ContentType t1 = ContentType.create("aaa/ccc");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testUnEqualTypesWithParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a");
    ContentType t2 = ContentType.create("ccc/bbb;x=y;a");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }


  @Test
  public void testUnEqualParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=y;a");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testUnEqualParametersCounts() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testUnEqualParametersCountsIgnoreQ() {
    ContentType t1 = ContentType.create("aaa/bbb;q=0.9");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testEqualParametersCountsIgnoreQ() {
    ContentType t1 = ContentType.create("aaa/bbb;q=0.9;x=y;a=b");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");
    
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testEqualParametersCountsWithQ() {
    ContentType t1 = ContentType.create("aaa", "bbb", addParameters("a", "b", "x", "y", "q", "0.9"));
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");
    
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testUnEqualWithUnsortedParameters() {
    ContentType t1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType t2 = ContentType.create("aaa/bbb;a=b;x=y");
    
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }
  
  @Test
  public void testCompareSame() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertEquals(0, t1.compareWildcardCounts(t2));
    assertEquals(0, t2.compareWildcardCounts(t1));
  }
  
  @Test
  public void testCompareOneWildcard() {
    ContentType t1 = ContentType.create("*/bbb");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertEquals(2, t1.compareWildcardCounts(t2));
    assertEquals(-2, t2.compareWildcardCounts(t1));
  }

  @Test
  public void testCompareTwoWildcard() {
    ContentType t1 = ContentType.create("*/*");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertEquals(3, t1.compareWildcardCounts(t2));
    assertEquals(-3, t2.compareWildcardCounts(t1));
  }

  @Test
  public void testCompareSubWildcard() {
    ContentType t1 = ContentType.create("aaa/*");
    ContentType t2 = ContentType.create("aaa/bbb");
    
    assertEquals(1, t1.compareWildcardCounts(t2));
    assertEquals(-1, t2.compareWildcardCounts(t1));
  }

  @Test
  public void testCompareCrossWildcard() {
    ContentType t1 = ContentType.create("aaa/*");
    ContentType t2 = ContentType.create("*/bbb");
    
    assertEquals(-1, t1.compareWildcardCounts(t2));
    assertEquals(1, t2.compareWildcardCounts(t1));
  }
}
