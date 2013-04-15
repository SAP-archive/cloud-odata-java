/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
* @author SAP AG
*/
public class Room {
  private final int id;
  private String name;
  private Integer seats;
  private Integer version;
  private Building building;
  private List<Employee> employees = new ArrayList<Employee>();

  public Room(final int id, final String name) {
    this.id = id;
    setName(name);
  }

  public String getId() {
    return Integer.toString(id);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setSeats(final int seats) {
    this.seats = seats;
  }

  public int getSeats() {
    return seats;
  }

  public void setVersion(final int version) {
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

  public List<Employee> getEmployees() {
    return employees;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj
        || obj != null && getClass() == obj.getClass() && id == ((Room) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"Seats\":" + seats + ",\"Version\":" + version + "}";
  }
}
