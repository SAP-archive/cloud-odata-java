package com.sap.core.odata.core.debug;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataContext.RuntimeMeasurement;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * Tests for the debug information output.
 * @author SAP AG
 */
public class ODataDebugResponseWrapperTest extends BaseTest {

  private static final String EXPECTED = "{"
      + "\"request\":{\"method\":\"GET\",\"uri\":\"http://test/entity\"},"
      + "\"response\":{\"status\":{\"code\":200,\"info\":\"OK\"}}}";

  private ODataContext mockContext(final ODataHttpMethod method) throws ODataException {
    ODataContext context = mock(ODataContext.class);
    when(context.getHttpMethod()).thenReturn(method.name());
    PathInfo pathInfo = mock(PathInfo.class);
    when(pathInfo.getRequestUri()).thenReturn(URI.create("http://test/entity"));
    when(context.getPathInfo()).thenReturn(pathInfo);
    when(context.getRuntimeMeasurements()).thenReturn(null);
    return context;
  }

  private ODataResponse mockResponse(final HttpStatusCodes status, final String body, final String contentType) {
    ODataResponse response = mock(ODataResponse.class);
    when(response.getStatus()).thenReturn(status);
    when(response.getEntity()).thenReturn(body);
    if (contentType != null) {
      when(response.getHeaderNames()).thenReturn(new HashSet<String>(Arrays.asList(HttpHeaders.CONTENT_TYPE)));
      when(response.getHeader(HttpHeaders.CONTENT_TYPE)).thenReturn(contentType);
      when(response.getContentHeader()).thenReturn(contentType);
    }
    return response;
  }

  private RuntimeMeasurement mockRuntimeMeasurement(final String method, final long startTime, final long stopTime, final long startMemory, final long stopMemory) {
    RuntimeMeasurement measurement = mock(RuntimeMeasurement.class);
    when(measurement.getClassName()).thenReturn("class");
    when(measurement.getMethodName()).thenReturn(method);
    when(measurement.getTimeStarted()).thenReturn(startTime);
    when(measurement.getTimeStopped()).thenReturn(stopTime);
    when(measurement.getMemoryStarted()).thenReturn(startMemory);
    when(measurement.getMemoryStopped()).thenReturn(stopMemory);
    return measurement;
  }

  private RuntimeMeasurement mockRuntimeMeasurement(final String method, final long start, final long stop) {
    return mockRuntimeMeasurement(method, start, stop, 1000, 4000);
  }

  @Test
  public void minimal() throws Exception {
    final ODataContext context = mockContext(ODataHttpMethod.PUT);
    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.NO_CONTENT, null, null);

    final ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String actualJson = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace(ODataHttpMethod.GET.name(), ODataHttpMethod.PUT.name())
        .replace(Integer.toString(HttpStatusCodes.OK.getStatusCode()), Integer.toString(HttpStatusCodes.NO_CONTENT.getStatusCode()))
        .replace(HttpStatusCodes.OK.getInfo(), HttpStatusCodes.NO_CONTENT.getInfo()),
        actualJson);
  }

  @Test
  public void body() throws Exception {
    final ODataContext context = mockContext(ODataHttpMethod.GET);
    ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.OK, "\"test\"", HttpContentType.APPLICATION_JSON);

    ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("{\"request", "{\"body\":\"test\",\"request")
        .replace("}}}", "},\"headers\":{\"" + HttpHeaders.CONTENT_TYPE + "\":\"" + HttpContentType.APPLICATION_JSON + "\"}}}"),
        entity);

    wrappedResponse = mockResponse(HttpStatusCodes.OK, "test", HttpContentType.TEXT_PLAIN);
    response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("{\"request", "{\"body\":\"test\",\"request")
        .replace("}}}", "},\"headers\":{\"" + HttpHeaders.CONTENT_TYPE + "\":\"" + HttpContentType.TEXT_PLAIN + "\"}}}"),
        entity);

    wrappedResponse = mockResponse(HttpStatusCodes.OK, null, "image/png");
    when(wrappedResponse.getEntity()).thenReturn(new ByteArrayInputStream("test".getBytes()));
    response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("{\"request", "{\"body\":\"dGVzdA==\",\"request")
        .replace("}}}", "},\"headers\":{\"" + HttpHeaders.CONTENT_TYPE + "\":\"image/png\"}}}"),
        entity);
  }

  @Test
  public void headers() throws Exception {
    ODataContext context = mockContext(ODataHttpMethod.GET);
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    headers.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(HttpContentType.APPLICATION_JSON));
    when(context.getRequestHeaders()).thenReturn(headers);

    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.OK, null, HttpContentType.APPLICATION_JSON);

    final ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("},\"response", ",\"headers\":{\"" + HttpHeaders.CONTENT_TYPE + "\":\"" + HttpContentType.APPLICATION_JSON + "\"}},\"response")
        .replace("}}}", "},\"headers\":{\"" + HttpHeaders.CONTENT_TYPE + "\":\"" + HttpContentType.APPLICATION_JSON + "\"}}}"),
        entity);
  }

  @Test
  public void uri() throws Exception {
    final ODataContext context = mockContext(ODataHttpMethod.GET);
    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.OK, null, null);

    UriInfo uriInfo = mock(UriInfo.class);
    final FilterExpression filter = UriParser.parseFilter(null, null, "true");
    when(uriInfo.getFilter()).thenReturn(filter);
    final OrderByExpression orderBy = UriParser.parseOrderBy(null, null, "true");
    when(uriInfo.getOrderBy()).thenReturn(orderBy);
    List<ArrayList<NavigationPropertySegment>> expand = new ArrayList<ArrayList<NavigationPropertySegment>>();
    NavigationPropertySegment segment = mock(NavigationPropertySegment.class);
    EdmNavigationProperty navigationProperty = mock(EdmNavigationProperty.class);
    when(navigationProperty.getName()).thenReturn("nav");
    when(segment.getNavigationProperty()).thenReturn(navigationProperty);
    ArrayList<NavigationPropertySegment> segments = new ArrayList<NavigationPropertySegment>();
    segments.add(segment);
    expand.add(segments);
    when(uriInfo.getExpand()).thenReturn(expand);
    SelectItem select1 = mock(SelectItem.class);
    SelectItem select2 = mock(SelectItem.class);
    EdmProperty property = mock(EdmProperty.class);
    when(property.getName()).thenReturn("property");
    when(select1.getProperty()).thenReturn(property);
    when(select2.getProperty()).thenReturn(property);
    when(select2.getNavigationPropertySegments()).thenReturn(segments);
    when(uriInfo.getSelect()).thenReturn(Arrays.asList(select1, select2));

    final ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, uriInfo, null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("}}}",
        "}},\"uri\":{\"filter\":{\"nodeType\":\"LITERAL\",\"type\":\"Edm.Boolean\",\"value\":\"true\"},"
            + "\"orderby\":{\"nodeType\":\"order collection\","
            + "\"orders\":[{\"nodeType\":\"ORDER\",\"sortorder\":\"asc\","
            + "\"expression\":{\"nodeType\":\"LITERAL\",\"type\":\"Edm.Boolean\",\"value\":\"true\"}}]},"
            + "\"expand/select\":{\"all\":false,\"properties\":[\"property\"],"
            + "\"links\":[{\"nav\":{\"all\":false,\"properties\":[\"property\"],\"links\":[]}}]}}}"),
        entity);
  }

  @Test
  public void uriWithException() throws Exception {
    final ODataContext context = mockContext(ODataHttpMethod.GET);
    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.OK, null, null);

    UriInfo uriInfo = mock(UriInfo.class);
    FilterExpression filter = mock(FilterExpression.class);
    when(filter.getExpressionString()).thenReturn("wrong");
    when(uriInfo.getFilter()).thenReturn(filter);
    ExpressionParserException exception = mock(ExpressionParserException.class);
    when(exception.getMessageReference()).thenReturn(ExpressionParserException.COMMON_ERROR);
    when(exception.getStackTrace()).thenReturn(new StackTraceElement[] {
        new StackTraceElement("class", "method", "file", 42) });
    CommonExpression filterTree = mock(CommonExpression.class);
    when(filterTree.getUriLiteral()).thenReturn("wrong");
    when(exception.getFilterTree()).thenReturn(filterTree);

    final ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, uriInfo, exception, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("}}}",
        "}},\"uri\":{\"error\":{\"filter\":\"wrong\"},\"filter\":null},"
            + "\"stacktrace\":{\"exceptions\":[{\"class\":\"" + exception.getClass().getName() + "\","
            + "\"message\":\"Error while parsing a ODATA expression.\","
            + "\"invocation\":{\"class\":\"class\",\"method\":\"method\",\"line\":42}}],"
            + "\"stacktrace\":[{\"class\":\"class\",\"method\":\"method\",\"line\":42}]}}"),
        entity);
  }

  @Test
  public void runtime() throws Exception {
    ODataContext context = mockContext(ODataHttpMethod.GET);
    List<RuntimeMeasurement> runtimeMeasurements = new ArrayList<RuntimeMeasurement>();
    runtimeMeasurements.add(mockRuntimeMeasurement("method", 1000, 42000));
    runtimeMeasurements.add(mockRuntimeMeasurement("inner", 2000, 5000));
    runtimeMeasurements.add(mockRuntimeMeasurement("inner", 7000, 12000));
    runtimeMeasurements.add(mockRuntimeMeasurement("inner", 13000, 16000));
    runtimeMeasurements.add(mockRuntimeMeasurement("inner2", 14000, 15000));
    runtimeMeasurements.add(mockRuntimeMeasurement("child", 17000, 21000));
    runtimeMeasurements.add(mockRuntimeMeasurement("second", 45000, 99000));
    when(context.getRuntimeMeasurements()).thenReturn(runtimeMeasurements);

    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.OK, null, null);

    ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), null, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED.replace("}}}",
        "}},\"runtime\":[{\"class\":\"class\",\"method\":\"method\",\"duration\":41,\"memory\":3,"
            + "\"children\":[{\"class\":\"class\",\"method\":\"inner\",\"duration\":8,\"memory\":6,\"children\":[]},"
            + "{\"class\":\"class\",\"method\":\"inner\",\"duration\":3,\"memory\":3,\"children\":["
            + "{\"class\":\"class\",\"method\":\"inner2\",\"duration\":1,\"memory\":3,\"children\":[]}]},"
            + "{\"class\":\"class\",\"method\":\"child\",\"duration\":4,\"memory\":3,\"children\":[]}]},"
            + "{\"class\":\"class\",\"method\":\"second\",\"duration\":54,\"memory\":3,\"children\":[]}]}"),
        entity);
  }

  @Test
  public void exception() throws Exception {
    final ODataContext context = mockContext(ODataHttpMethod.GET);
    final ODataResponse wrappedResponse = mockResponse(HttpStatusCodes.BAD_REQUEST, null, null);

    ODataMessageException exception = mock(ODataMessageException.class);
    when(exception.getMessageReference()).thenReturn(ODataMessageException.COMMON);
    RuntimeException innerException = mock(RuntimeException.class);
    when(innerException.getLocalizedMessage()).thenReturn("error");
    final StackTraceElement[] stackTrace = new StackTraceElement[] {
        new StackTraceElement("innerClass", "innerMethod", "innerFile", 99),
        new StackTraceElement("class", "method", "file", 42),
        new StackTraceElement("outerClass", "outerMethod", "outerFile", 11) };
    when(innerException.getStackTrace()).thenReturn(stackTrace);
    when(exception.getCause()).thenReturn(innerException);
    when(exception.getStackTrace()).thenReturn(Arrays.copyOfRange(stackTrace, 1, 3));

    final ODataResponse response = new ODataDebugResponseWrapper(context, wrappedResponse, mock(UriInfo.class), exception, ODataDebugResponseWrapper.ODATA_DEBUG_JSON)
        .wrapResponse();
    String entity = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals(EXPECTED
        .replace(Integer.toString(HttpStatusCodes.OK.getStatusCode()), Integer.toString(HttpStatusCodes.BAD_REQUEST.getStatusCode()))
        .replace(HttpStatusCodes.OK.getInfo(), HttpStatusCodes.BAD_REQUEST.getInfo())
        .replace("}}}", "}},"
            + "\"stacktrace\":{\"exceptions\":[{\"class\":\"" + exception.getClass().getName() + "\","
            + "\"message\":\"Common exception\","
            + "\"invocation\":{\"class\":\"class\",\"method\":\"method\",\"line\":42}},"
            + "{\"class\":\"" + innerException.getClass().getName() + "\",\"message\":\"error\","
            + "\"invocation\":{\"class\":\"innerClass\",\"method\":\"innerMethod\",\"line\":99}}],"
            + "\"stacktrace\":[{\"class\":\"class\",\"method\":\"method\",\"line\":42},"
            + "{\"class\":\"outerClass\",\"method\":\"outerMethod\",\"line\":11}]}}"),
        entity);
  }
}
