package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
* @author SAP AG
*/
public class Room {
  private static int counter = 1;
  private int id;
  private String name;
  private Integer seats;
  private Integer version;
  private Building building;
  private List<Employee> employees = new ArrayList<Employee>();

  public Room() {
    this(null);
  }

  public Room(final String name) {
    id = counter++;
    setName(name);
  }

  public Room(final String name, final int seats) {
    this(name);
    setSeats(seats);
  }

  public String getId() {
    return Integer.toString(id);
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setSeats(final Integer seats) {
    this.seats = seats;
  }

  public int getSeats() {
    return seats;
  }

  public void setVersion(final Integer version) {
    this.version = version;
  }

  public int getVersion() {
    return version;
  }

  public void setBuilding(final Building building) {
    this.building = building;
  }

  public Building getBuilding() {
    return building;
  }

  public void setEmployees(final List<Employee> employeesList) {
    this.employees = employeesList;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public static void reset() {
    counter = 1;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    return id == ((Room) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"Seats\":" + seats + ",\"Version\":" + version + "}";
  }
}
