package org.odata4j.examples.producer.jpa.airline;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Flight {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  private Long flightID;

  @OneToOne
  private FlightSchedule flightSchedule;

  private Timestamp takeoffTime;

  public FlightSchedule getFlightSchedule() {
    return flightSchedule;
  }

  public void setFlightSchedule(FlightSchedule flightSchedule) {
    this.flightSchedule = flightSchedule;
  }

  public Timestamp getTakeoffTime() {
    return takeoffTime;
  }

  public void setTakeoffTime(Timestamp takeoffTime) {
    this.takeoffTime = takeoffTime;
  }

  public Long getFlightID() {
    return flightID;
  }

}
