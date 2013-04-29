package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.testutil.fit.BaseTest;

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

public class ContentTypeTest extends BaseTest {

  @Test
  public void testMe() {
    MediaType t = new MediaType("*", "xml");
    assertNotNull(t);
    assertTrue(t.isCompatible(new MediaType("app", "xml")));
  }

  @Test
  public void parseable() {
    assertTrue(ContentType.isParseable("application/xml"));
    assertTrue(ContentType.isParseable("text/plain"));
    assertTrue(ContentType.isParseable("application/atom+xml; charset=UTF-8"));

    assertFalse(ContentType.isParseable("app/app/moreapp"));
    //assertFalse(ContentType.isParseable("application/atom+xml; charset   =   UTF-8"));
    assertFalse(ContentType.isParseable(null));
  }

  @Test
  public void parseNotThrow() {
    assertNotNull(ContentType.parse("application/xml"));
    assertNotNull(ContentType.parse("text/plain"));
    assertNotNull(ContentType.parse("application/atom+xml; charset=UTF-8"));

    assertNull(ContentType.parse("app/app/moreapp"));
    //assertFalse(ContentType.isParseable("application/atom+xml; charset   =   UTF-8"));
    assertNull(ContentType.parse(null));
  }

  @Test
  public void creationFromHttpContentTypeAtomXmlEntry() {
    ContentType mt = ContentType.create(HttpContentType.APPLICATION_ATOM_XML_ENTRY_UTF8);

    assertEquals("application", mt.getType());
    assertEquals("atom+xml", mt.getSubtype());
    assertEquals("application/atom+xml; charset=utf-8; type=entry", mt.toString());
    assertEquals(ODataFormat.ATOM, mt.getODataFormat());
    assertEquals(2, mt.getParameters().size());
    assertEquals("entry", mt.getParameters().get("type"));
    assertEquals("utf-8", mt.getParameters().get("charset"));
    assertEquals(ContentType.APPLICATION_ATOM_XML_ENTRY_CS_UTF_8, mt);
  }

  @Test
  public void creationFromHttpContentTypeMultipartMixed() {
    ContentType mt = ContentType.create(HttpContentType.MULTIPART_MIXED);

    assertEquals("multipart", mt.getType());
    assertEquals("mixed", mt.getSubtype());
    assertEquals("multipart/mixed", mt.toString());
    assertEquals(ODataFormat.CUSTOM, mt.getODataFormat());
    assertEquals(0, mt.getParameters().size());
    assertEquals(ContentType.MULTIPART_MIXED, mt);
  }

  @Test
  public void creationFromHttpContentTypeApplicationXml() {
    ContentType mt = ContentType.create(HttpContentType.APPLICATION_XML_UTF8);

    assertEquals("application", mt.getType());
    assertEquals("xml", mt.getSubtype());
    assertEquals("application/xml; charset=utf-8", mt.toString());
    assertEquals(ODataFormat.XML, mt.getODataFormat());
    assertEquals(1, mt.getParameters().size());
    assertEquals(ContentType.create(ContentType.APPLICATION_XML, "charset", "utf-8"), mt);
  }

  @Test
  public void creationFromHttpContentTypeApplicationJson() {
    ContentType mt = ContentType.create(HttpContentType.APPLICATION_JSON_UTF8);

    assertEquals("application", mt.getType());
    assertEquals("json", mt.getSubtype());
    assertEquals("application/json; charset=utf-8", mt.toString());
    assertEquals(ODataFormat.JSON, mt.getODataFormat());
    assertEquals(1, mt.getParameters().size());
    assertEquals(ContentType.create(ContentType.APPLICATION_JSON, "charset", "utf-8"), mt);
  }

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

  private Map<String, String> addParameters(final String... content) {
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < content.length - 1; i += 2) {
      String key = content[i];
      String value = content[i + 1];
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
  public void testEqualWithParametersIgnoreCase() {
    ContentType t1 = ContentType.create("aaa/bbb;x=YY");
    ContentType t2 = ContentType.create("aaa/bbb;x=yy");

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
  public void testEqualWithUnsortedParametersIgnoreCase() {
    ContentType t1 = ContentType.create("aaa/bbb;xx=y;a=BB");
    ContentType t2 = ContentType.create("aaa/bbb;a=bb;XX=y");

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

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSubTypeWildcardSubtype() {
    ContentType t1 = ContentType.create("*/bbb");
    assertNull(t1);
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
  public void testCompareTwoWildcard() {
    ContentType t1 = ContentType.create("*/*");
    ContentType t2 = ContentType.create("aaa/bbb");

    assertEquals(3, t1.compareWildcardCounts(t2));
    assertEquals(-3, t2.compareWildcardCounts(t1));
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testCompareSubWildcard() {
    ContentType t1 = ContentType.create("aaa/*");
    ContentType t2 = ContentType.create("aaa/bbb");

    assertEquals(1, t1.compareWildcardCounts(t2));
    assertEquals(-1, t2.compareWildcardCounts(t1));
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }

  @Test
  public void testCompareSubTypeWildcard() {
    ContentType t1 = ContentType.create("aaa/*");
    ContentType t2 = ContentType.create("xxx/*");

    assertEquals(0, t1.compareWildcardCounts(t2));
    assertEquals(0, t2.compareWildcardCounts(t1));
    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t1));
  }

  @Test
  public void testNonEqualCharset() {
    ContentType t1 = ContentType.create("aaa/bbb;charset=c1");
    ContentType t2 = ContentType.create("aaa/bbb;charset=c2");

    assertFalse(t1.equals(t2));
  }

  @Test
  public void testCompatible() {
    ContentType t1 = ContentType.create("aaa/bbb");
    ContentType t2 = ContentType.create("aaa/bbb");

    assertTrue(t1.isCompatible(t2));
    assertTrue(t2.isCompatible(t1));
    //
    assertTrue(t1.equals(t2));
  }

  @Test
  public void testCompatibleQ_ParametersSet() {
    ContentType t1 = ContentType.create("aaa/bbb;q=0.9;x=y;a=b");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");

    assertTrue(t1.isCompatible(t2));
    assertTrue(t2.isCompatible(t1));
    //
    assertTrue(t1.equals(t2));
  }

  @Test
  public void testCompatibleDiffParameterValuesSet() {
    ContentType t1 = ContentType.create("aaa/bbb;x=z;a=c");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");

    assertTrue(t1.isCompatible(t2));
    assertTrue(t2.isCompatible(t1));
    //
    assertFalse(t1.equals(t2));
  }
  
  @Test
  public void testCompatibleDiffParameterCountSet() {
    ContentType t1 = ContentType.create("aaa/bbb;a=b");
    ContentType t2 = ContentType.create("aaa/bbb;x=y;a=b");

    assertTrue(t1.isCompatible(t2));
    assertTrue(t2.isCompatible(t1));
    //
    assertFalse(t1.equals(t2));
  }

  @Test
  public void testMatchSimple() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("foo/me");

    ContentType match = check.match(toMatchContentTypes);

    assertEquals(ContentType.create("foo/me"), match);
    assertEquals("foo/me", match.toContentTypeString());
  }

  @Test
  public void testMatchNoMatch() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("for/me");

    ContentType match = check.match(toMatchContentTypes);

    assertTrue(match == null);
  }

  @Test
  public void testHasMatchSimple() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("foo/me");

    boolean match = check.hasMatch(toMatchContentTypes);
    assertTrue(match);
  }

  @Test
  public void testHasMatchNoMatch() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("for/me");

    boolean match = check.hasMatch(toMatchContentTypes);
    assertFalse(match);
  }

  @Test
  public void testMatchCompatibleSimple() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("foo/me");

    ContentType match = check.matchCompatible(toMatchContentTypes);

    assertEquals(ContentType.create("foo/me"), match);
    assertEquals("foo/me", match.toContentTypeString());
  }

  @Test
  public void testMatchCompatibleNoMatch() {
    ContentType m1 = ContentType.create("aaa/bbb;x=z;a=b");
    ContentType m2 = ContentType.create("aaa/ccc");
    ContentType m3 = ContentType.create("foo/me");
    List<ContentType> toMatchContentTypes = new ArrayList<ContentType>();
    toMatchContentTypes.add(m1);
    toMatchContentTypes.add(m2);
    toMatchContentTypes.add(m3);

    ContentType check = ContentType.create("for/me");

    ContentType match = check.matchCompatible(toMatchContentTypes);

    assertTrue(match == null);
  }
}
