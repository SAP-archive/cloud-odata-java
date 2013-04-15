/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
* @author SAP AG
*/
public class Team {
  private final int id;
  private String name;
  private Boolean isScrumTeam;
  private List<Employee> employees = new ArrayList<Employee>();

  public Team(final int id, final String name) {
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

  public boolean isScrumTeam() {
    return isScrumTeam;
  }

  public void setScrumTeam(final boolean isScrumTeam) {
    this.isScrumTeam = isScrumTeam;
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
        || obj != null && getClass() == obj.getClass() && id == ((Team) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"isScrumTeam\":" + isScrumTeam + "}";
  }
}
