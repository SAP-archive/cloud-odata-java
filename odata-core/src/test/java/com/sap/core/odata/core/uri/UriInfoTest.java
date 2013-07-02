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
package com.sap.core.odata.core.uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriNotMatchingException;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class UriInfoTest extends BaseTest {

  private static Edm edm;

  @BeforeClass
  public static void getEdm() throws ODataException, EdmException {
    edm = MockFacade.getMockEdm();
  }

  /**
    * Parse the URI part after an OData service root, given as string.
    * Query parameters can be included.
    * @param uri  the URI part
    * @return a {@link UriInfoImpl} instance containing the parsed information
    */
  private UriInfoImpl parse(final String uri) throws UriSyntaxException, UriNotMatchingException, EdmException {
    final String[] path = uri.split("\\?", -1);
    if (path.length > 2) {
      throw new UriSyntaxException(UriSyntaxException.URISYNTAX);
    }

    final List<PathSegment> pathSegments = getPathSegments(path[0]);
    Map<String, String> queryParameters;
    if (path.length == 2) {
      queryParameters = getQueryParameters(unescape(path[1]));
    } else {
      queryParameters = new HashMap<String, String>();
    }

    UriInfo result = new UriParserImpl(edm).parse(pathSegments, queryParameters);

    return (UriInfoImpl) result;
  }

  private List<PathSegment> getPathSegments(final String uri) throws UriSyntaxException {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : uri.split("/", -1)) {
      final String unescapedSegment = unescape(segment);
      PathSegment oDataSegment = new ODataPathSegmentImpl(unescapedSegment, null);
      pathSegments.add(oDataSegment);
    }
    return pathSegments;
  }

  private Map<String, String> getQueryParameters(final String uri) {
    Map<String, String> queryParameters = new HashMap<String, String>();
    for (final String option : uri.split("&")) {
      final String[] keyAndValue = option.split("=");
      if (keyAndValue.length == 2) {
        queryParameters.put(keyAndValue[0], keyAndValue[1]);
      } else {
        queryParameters.put(keyAndValue[0], "");
      }
    }
    return queryParameters;
  }

  private String unescape(final String s) throws UriSyntaxException {
    try {
      return new URI(s).getPath();
    } catch (URISyntaxException e) {
      throw new UriSyntaxException(UriSyntaxException.NOTEXT);
    }
  }

  @Test
  public void everythingInitial() throws Exception {
    UriInfoImpl result = parse("/");

    assertEquals(Collections.emptyList(), result.getKeyPredicates());
    assertEquals(Collections.emptyList(), result.getTargetKeyPredicates());
    assertEquals(Collections.emptyList(), result.getNavigationSegments());
    assertEquals(Collections.emptyList(), result.getPropertyPath());
    assertEquals(Collections.emptyList(), result.getExpand());
    assertEquals(Collections.emptyList(), result.getSelect());
    assertEquals(Collections.emptyMap(), result.getFunctionImportParameters());
    assertEquals(Collections.emptyMap(), result.getCustomQueryOptions());
    assertNull(result.getEntityContainer());
    assertNull(result.getStartEntitySet());
    assertNull(result.getTargetEntitySet());
    assertNull(result.getFunctionImport());
    assertNull(result.getTargetType());
    assertNull(result.getFormat());
    assertNull(result.getFilter());
    assertNull(result.getInlineCount());
    assertNull(result.getOrderBy());
    assertNull(result.getSkipToken());
    assertNull(result.getSkip());
    assertNull(result.getTop());
  }

  @Test
  public void allInitial() throws Exception {
    UriInfoImpl result = parse("/Employees");

    assertEquals(Collections.emptyList(), result.getKeyPredicates());
    assertEquals(Collections.emptyList(), result.getTargetKeyPredicates());
    assertEquals(Collections.emptyList(), result.getNavigationSegments());
    assertEquals(Collections.emptyList(), result.getPropertyPath());
    assertEquals(Collections.emptyList(), result.getExpand());
    assertEquals(Collections.emptyList(), result.getSelect());
    assertEquals(Collections.emptyMap(), result.getFunctionImportParameters());
    assertEquals(Collections.emptyMap(), result.getCustomQueryOptions());
  }

  @Test
  public void someInitial() throws Exception {
    UriInfoImpl result = parse("/Employees('1')");

    assertNotSame(Collections.emptyList(), result.getKeyPredicates());
    assertNotSame(Collections.emptyList(), result.getTargetKeyPredicates());

    assertEquals(Collections.emptyList(), result.getNavigationSegments());
    assertEquals(Collections.emptyList(), result.getPropertyPath());
    assertEquals(Collections.emptyList(), result.getExpand());
    assertEquals(Collections.emptyList(), result.getSelect());
    assertEquals(Collections.emptyMap(), result.getFunctionImportParameters());
    assertEquals(Collections.emptyMap(), result.getCustomQueryOptions());
  }

  @Test
  public void someInitial2() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/ne_Manager");

    assertNotSame(Collections.emptyList(), result.getKeyPredicates());
    assertNotSame(Collections.emptyList(), result.getNavigationSegments());

    assertEquals(Collections.emptyList(), result.getTargetKeyPredicates());
    assertEquals(Collections.emptyList(), result.getPropertyPath());
    assertEquals(Collections.emptyList(), result.getExpand());
    assertEquals(Collections.emptyList(), result.getSelect());
    assertEquals(Collections.emptyMap(), result.getFunctionImportParameters());
    assertEquals(Collections.emptyMap(), result.getCustomQueryOptions());
  }

}
