package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.annotations.EdmEntity;
import com.sap.core.odata.api.annotations.EdmProperty;

@EdmEntity(name="Team")
public class Team {
  private static int counter = 1;
  private int id;
  private String name;
  @EdmProperty(name="isScrumTeam", facet="")
  private boolean isScrumTeam;
  private List<Employee> employees;

  public Team() {
    this(null);
  }

  public Team(String name) {
    id = counter++;
    employees = new ArrayList<Employee>();
    setName(name);

  }

  public Team(String name, boolean isScrumTeam) {
    this(name);
    this.setScrumTeam(isScrumTeam);
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

  public boolean isScrumTeam() {
    return isScrumTeam;
  }

  public void setScrumTeam(boolean isScrumTeam) {
    this.isScrumTeam = isScrumTeam;
  }

  public void setEmployees(List<Employee> employees) {
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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Team other = (Team) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"isScrumTeam\":" + isScrumTeam + "}";
  }
}
