package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SAP AG
 */
public class Manager extends Employee {

  private List<Employee> employees = new ArrayList<Employee>();

  public Manager(final int id, final String name) {
    super(id, name);
  }

  public void setEmployees(final List<Employee> employees) {
    this.employees = employees;
  }

  public List<Employee> getEmployees() {
    return employees;
  }
}
