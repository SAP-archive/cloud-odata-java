package org.odata4j.examples.producer.jpa.airline;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class FlightSchedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  private Long flightScheduleID;

  @Column(nullable = false)
  private String flightNo;

  @Temporal(TemporalType.TIME)
  @Column(nullable = false, length = 8)
  private Date departureTime;

  @OneToOne
  @JoinColumn(name = "DEPARTUREAIRPORT_CODE")
  private Airport departureAirport;

  @Column(name = "DEPARTUREAIRPORT_CODE", insertable = false, updatable = false)
  private String departureAirportCode;

  @Column(nullable = false)
  private Time arrivalTime;

  @OneToOne
  @JoinColumn(name = "ARRIVALAIRPORT_CODE")
  private Airport arrivalAirport;

  @Column(name = "ARRIVALAIRPORT_CODE", insertable = false, updatable = false)
  private String arrivalAirportCode;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Calendar firstDeparture;

  @Temporal(TemporalType.DATE)
  private Date lastDeparture;

  public String getFlightNo() {
    return flightNo;
  }

  public void setFlightNo(String flightNo) {
    this.flightNo = flightNo;
  }

  public Date getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(Date departureTime) {
    this.departureTime = departureTime;
  }

  public Airport getDepartureAirport() {
    return departureAirport;
  }

  public void setDepartureAirport(Airport departureAirport) {
    this.departureAirport = departureAirport;
  }

  public String getDepartureAirportCode() {
    return departureAirportCode;
  }

  public Time getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(Time arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public Airport getArrivalAirport() {
    return arrivalAirport;
  }

  public void setArrivalAirport(Airport arrivalAirport) {
    this.arrivalAirport = arrivalAirport;
  }

  public String getArrivalAirportCode() {
    return arrivalAirportCode;
  }

  public Calendar getFirstDeparture() {
    return firstDeparture;
  }

  public void setFirstDeparture(Calendar firstDeparture) {
    this.firstDeparture = firstDeparture;
  }

  public Date getLastDeparture() {
    return lastDeparture;
  }

  public void setLastDeparture(Date lastDeparture) {
    this.lastDeparture = lastDeparture;
  }

  public Long getFlightScheduleID() {
    return flightScheduleID;
  }

}
