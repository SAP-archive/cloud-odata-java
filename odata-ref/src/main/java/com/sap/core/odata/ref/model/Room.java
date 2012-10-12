package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
  private static int counter = 1;
  private int id;
  private String name;
  private int seats;
  private int version;
  private Building building;
  private List<Employee> employees = new ArrayList<Employee>();

  public Room() {
    this(null);
  }

  public Room(String name) {
    id = counter++;
    setName(name);
  }

  public Room(String name, int seats) {
    this(name);
    this.setSeats(seats);
  }

  public String getId() {
    return Integer.toString(id);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSeats(int seats) {
    this.seats = seats;
  }

  public int getSeats() {
    return seats;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getVersion() {
    return version;
  }

  public void setBuilding(Building building) {
    this.building = building;
    this.building.getRooms().add(this);
  }

  public Building getBuilding() {
    return building;
  }

  public void setEmployees(List<Employee> employeesList) {
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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Room other = (Room) obj;
    if (id != other.id)
      return false;
    return true;
  }

}
