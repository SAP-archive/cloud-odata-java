package org.odata4j.test.integration.producer.jpa.airline;

import java.text.DateFormat;
import java.util.Locale;

import javax.persistence.Persistence;

import org.core4j.Enumerable;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.examples.producer.jpa.JPAProvider;
import org.odata4j.producer.jpa.JPAProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;

public class EdmTimeTemporalTest extends AirlineJPAProducerBaseTest {

  public EdmTimeTemporalTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    String persistenceUnitName = "AirlineService" + JPAProvider.JPA_PROVIDER.caption;
    String namespace = "Airline";

    emf = Persistence.createEntityManagerFactory(persistenceUnitName);

    JPAProducer producer = new JPAProducer(emf, namespace, 20);

    DefaultODataProducerProvider.setInstance(producer);

    server = this.rtFacade.startODataServer(endpointUri);
    this.fillDatabase();

  }

  @Test
  public void testMetadata() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    EdmDataServices metadata = consumer.getMetadata();

    Assert.assertEquals(EdmSimpleType.TIME, metadata.findEdmEntitySet("FlightSchedule").getType().findProperty("departureTime").getType());
    Assert.assertEquals(EdmSimpleType.TIME, metadata.findEdmEntitySet("FlightSchedule").getType().findProperty("arrivalTime").getType());
  }

  @Test
  /**
   *handling of Date fields with different @Temporal
   */
  public void createWithDifferentTemporal() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    OEntity flightSchedule = consumer.createEntity("FlightSchedule")
        .properties(OProperties.string("flightNo", "LH460"))
        .properties(OProperties.time("departureTime", new LocalTime(9, 30, 0)))
        .properties(OProperties.time("arrivalTime", DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).parse("2:10 pm")))
        .properties(OProperties.datetime("firstDeparture", new LocalDateTime(2011, 03, 28, 9, 30)))
        .properties(OProperties.datetime("lastDeparture", DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse("07/05/2011")))
        .execute();

    Long id = (Long) flightSchedule.getProperty("flightScheduleID").getValue();
    Assert.assertEquals(new LocalTime(9, 30, 0), flightSchedule.getProperty("departureTime").getValue());
    Assert.assertEquals(new LocalTime(14, 10, 0), flightSchedule.getProperty("arrivalTime").getValue());
    Assert.assertEquals(new LocalDateTime(2011, 03, 28, 9, 30), flightSchedule.getProperty("firstDeparture").getValue());
    Assert.assertEquals(new LocalDateTime(2011, 07, 05, 0, 0), flightSchedule.getProperty("lastDeparture").getValue());

    flightSchedule = consumer.getEntity("FlightSchedule", id).execute();
    Assert.assertEquals(new LocalTime(9, 30, 0), flightSchedule.getProperty("departureTime").getValue());
    Assert.assertEquals(new LocalTime(14, 10, 0), flightSchedule.getProperty("arrivalTime").getValue());
    Assert.assertEquals(new LocalDateTime(2011, 03, 28, 9, 30), flightSchedule.getProperty("firstDeparture").getValue());
    Assert.assertEquals(new LocalDateTime(2011, 07, 05, 0, 0), flightSchedule.getProperty("lastDeparture").getValue());
  }

  @Test
  public void filterTime() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    Enumerable<OEntity> schedules = consumer.getEntities("FlightSchedule")
        .filter("departureTime ge time'PT11H' and departureTime lt time'PT12H'")
        .execute();

    Assert.assertEquals(1, schedules.count());

  }
}
