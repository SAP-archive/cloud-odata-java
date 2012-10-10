package org.odata4j.test.integration.expressions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;

public class DateTimeFormatTest extends AbstractRuntimeTest {

  public DateTimeFormatTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void testDateTimeRoundtrip() throws Exception {
    String endpointUri = "http://localhost:8810/DateTimeFormatTest.svc/";

    final long now = 1292865839424L;

    InMemoryProducer producer = new InMemoryProducer("DateTimeRoundtrip");

    producer.register(DateTimeRoundtrip.class, "DateTimeRoundtrip",
        new Func<Iterable<DateTimeRoundtrip>>() {
          @Override
          public Iterable<DateTimeRoundtrip> apply() {
            return Enumerable.create(new DateTimeRoundtrip(1, new Date(now)));
          }
        }, "Key");
    DefaultODataProducerProvider.setInstance(producer);
    ODataServer server = this.rtFacade.startODataServer(endpointUri);
    ODataConsumer c = this.rtFacade.createODataConsumer(endpointUri, null);
    List<OEntity> oentities = c.getEntities("DateTimeRoundtrip").execute().toList();

    Assert.assertEquals(1, oentities.size());
    // preserve milliseconds
    Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date(now)),
        oentities.get(0).getProperty("Date").getValue().toString());

    server.stop();
  }

  class DateTimeRoundtrip {
    public DateTimeRoundtrip(long key, Date date) {
      this.key = key;
      this.date = date;
    }

    public long key;
    public Date date;

    public long getKey() {
      return key;
    }

    public void setKey(long key) {
      this.key = key;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }
  }
}
