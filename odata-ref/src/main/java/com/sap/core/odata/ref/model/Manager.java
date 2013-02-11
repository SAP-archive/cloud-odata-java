package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SAP AG
 */
public class Manager extends Employee {

  private List<Employee> employees = new ArrayList<Employee>();

  public Manager(int id) {
    this(id, null, 0);
  }

  public Manager(int id, final String name, final int age) {
    super(id, name, age);
  }

  public Manager(int id, final String name, final int age, final Room room, final Team team) {
    super(id, name, age, room, team);
  }

  public void setEmployees(final List<Employee> employees) {
    this.employees = employees;
  }

  public List<Employee> getEmployees() {
    return employees;
  }
}
