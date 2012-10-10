package org.odata4j.test.unit.format.xml;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.odata4j.format.FormatType;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.InlineCount;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.inmemory.InMemoryProducer;

@RunWith(value = Parameterized.class)
public class AtomFeedFormatWriterTest {
  private static final String BASE_URI = "http://www.test.com/OData";
  private static final String SIMPLE_ENTITIES_PATH = "/SimpleEntities";
  private static final String SIMPLE_ENTITIES_TOP_PATH = SIMPLE_ENTITIES_PATH + "?$top=";

  private static final long NUM_ENTITIES = 200;

  private static FormatWriter<EntitiesResponse> formatWriter;

  private URI uri;
  private UriInfo uriInfoMock;
  private UriBuilder uriBuilderMock;
  private StringWriter stringWriter;

  private int top;

  public AtomFeedFormatWriterTest(int top) {
    this.top = top;
  }

  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] { { 50 }, { 100 }, { 150 }, { 200 } };
    return Arrays.asList(data);
  }

  @BeforeClass
  public static void setupClass() throws Exception {
    formatWriter = FormatWriterFactory.getFormatWriter(EntitiesResponse.class, null, FormatType.ATOM.toString(), null);
  }

  @Before
  public void setup() throws Exception {
    uri = new URI(BASE_URI + SIMPLE_ENTITIES_TOP_PATH + top);
    uriBuilderMock = createUriBuilderMock();
    uriInfoMock = createUriInfoMock();
    stringWriter = new StringWriter();
  }

  private UriBuilder createUriBuilderMock() {
    final UriBuilder uriBuilderMock = mock(UriBuilder.class);
    when(uriBuilderMock.replaceQueryParam(eq("$skiptoken"), anyString())).thenAnswer(new Answer<UriBuilder>() {

      @Override
      public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
        String skipToken = (String) invocation.getArguments()[1];
        uri = new URI(uri.toString() + "&$skiptoken=" + skipToken);
        return uriBuilderMock;
      }

    });
    when(uriBuilderMock.replaceQueryParam(eq("$top"), anyLong())).thenAnswer(new Answer<UriBuilder>() {

      @Override
      public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
        long topx = (Long) invocation.getArguments()[1];
        uri = new URI(uri.toString().replace("$top=" + top, "$top=" + topx));
        return uriBuilderMock;
      }

    });
    when(uriBuilderMock.replaceQueryParam(eq("$top"))).thenAnswer(new Answer<UriBuilder>() {

      @Override
      public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
        uri = new URI(uri.toString().replace("$top=" + top + "&", ""));
        return uriBuilderMock;
      }

    });
    when(uriBuilderMock.build()).then(new Answer<URI>() {

      @Override
      public URI answer(InvocationOnMock invocation) throws Throwable {
        return uri;
      }

    });
    return uriBuilderMock;
  }

  @SuppressWarnings("unchecked")
  private UriInfo createUriInfoMock() throws URISyntaxException {
    MultivaluedMap<String, String> queryParametersMock = mock(MultivaluedMap.class);
    when(queryParametersMock.get("$top")).thenReturn(Arrays.asList(String.valueOf(top)));

    UriInfo uriInfoMock = mock(UriInfo.class);
    when(uriInfoMock.getBaseUri()).thenReturn(new URI(BASE_URI));
    when(uriInfoMock.getPath()).thenReturn(SIMPLE_ENTITIES_TOP_PATH + top);
    when(uriInfoMock.getRequestUriBuilder()).thenReturn(uriBuilderMock);
    when(uriInfoMock.getQueryParameters()).thenReturn(queryParametersMock);

    return uriInfoMock;
  }

  // Test for Issue #94
  @Test
  public void testNextUrl() {
    InMemoryProducer p = new InMemoryProducer("testNextUrl");
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        SimpleEntity[] entities = new SimpleEntity[(int) NUM_ENTITIES];
        for (int i = 0; i < NUM_ENTITIES; i++) {
          entities[i] = new SimpleEntity(i);
        }
        return Enumerable.create(entities);
      }
    }, "Id");

    EntitiesResponse response = p.getEntities(null, "setName", new QueryInfo(InlineCount.NONE, top, null, null, null, null, null, null, null));
    formatWriter.write(uriInfoMock, stringWriter, response);
    String s = stringWriter.toString();
    verify(uriBuilderMock, times(1)).replaceQueryParam(eq("$skiptoken"), anyString());
    long newTop = top - response.getEntities().size();
    if (newTop > 0) {
      verify(uriBuilderMock, times(1)).replaceQueryParam(eq("$top"), eq(newTop));
      // The next url in this case should be '?$top=50&$skiptoken=<token>' and not '?$top=150&$skiptoken=<token>'
      assertEquals("<link rel=\"next\" href=\"" + BASE_URI + SIMPLE_ENTITIES_TOP_PATH + newTop +
          "&amp;$skiptoken=" + response.getSkipToken() + "\"></link></feed>", s.substring(s.indexOf("<link rel=\"next\"")));
    } else {
      verify(uriBuilderMock, times(1)).replaceQueryParam(eq("$top"));
      assertEquals("<link rel=\"next\" href=\"" + BASE_URI + SIMPLE_ENTITIES_PATH + "?" +
          "$skiptoken=" + response.getSkipToken() + "\"></link></feed>", s.substring(s.indexOf("<link rel=\"next\"")));
    }
  }

  @SuppressWarnings("unused")
  private static class SimpleEntity {
    private final int integer;

    public SimpleEntity(int integer) {
      this.integer = integer;
    }

    public String getId() {
      return String.valueOf(System.identityHashCode(this));
    }
  }

}
