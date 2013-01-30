package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SAP AG
 */
public class Manager extends Employee {

  private List<Employee> employees = new ArrayList<Employee>();

  public Manager() {
    this(null, 0);
  }

  public Manager(final String name, final int age) {
    super(name, age);
  }

  public Manager(final String name, final int age, final Room room, final Team team) {
    super(name, age, room, team);
  }

  public void setEmployees(final List<Employee> employees) {
    this.employees = employees;
  }

  public List<Employee> getEmployees() {
    return employees;
  }
}
