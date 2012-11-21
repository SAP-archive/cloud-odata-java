package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.annotations.EdmEntity;
import com.sap.core.odata.api.annotations.EdmType;
import com.sap.core.odata.api.annotations.EdmTypeKind;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;

@EdmEntity(name="Manager", type=@EdmType(value=EdmTypeKind.COMPLEX, namespace=ScenarioEdmProvider.NAMESPACE_1))
public class Manager extends Employee {

  private List<Employee> employees = new ArrayList<Employee>();

  public Manager() {
    this(null, 0);
  }

  public Manager(String name, int age) {
    super(name, age);
  }

  public Manager(String name, int age, Room room, Team team) {
    super(name, age, room, team);
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

  public List<Employee> getEmployees() {
    return employees;
  }
}
