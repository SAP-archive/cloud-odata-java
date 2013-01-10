package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
* @author SAP AG
*/
public class Team {
  private static int counter = 1;
  private int id;
  private String name;
  private boolean isScrumTeam;
  private List<Employee> employees;

  public Team() {
    this(null);
  }

  public Team(final String name) {
    id = counter++;
    employees = new ArrayList<Employee>();
    setName(name);
  }

  public Team(final String name, final boolean isScrumTeam) {
    this(name);
    setScrumTeam(isScrumTeam);
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

  public boolean isScrumTeam() {
    return isScrumTeam;
  }

  public void setScrumTeam(final boolean isScrumTeam) {
    this.isScrumTeam = isScrumTeam;
  }

  public void setEmployees(final List<Employee> employees) {
    this.employees = employees;
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

    return id == ((Team) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"isScrumTeam\":" + isScrumTeam + "}";
  }
}
