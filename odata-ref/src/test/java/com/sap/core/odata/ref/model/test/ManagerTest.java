package com.sap.core.odata.ref.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

public class ManagerTest {

  private static final String VALUE_NAME = "Peter Schulz";
  private static final String TEAM_NAME = "Team 1";

  @Test
  public void testId() {
    Manager manager = new Manager();
    String testMngId = manager.getId();
    assertNotNull(testMngId);
  }

  @Test
  public void testManagerName() {
    Manager manager = new Manager();
    manager.setEmployeeName(VALUE_NAME);
    String testName = manager.getEmployeeName();
    assertEquals(VALUE_NAME, testName);
  }

  @Test
  public void testManager() {
    Manager manager = new Manager("Walter Winter", 52);
    Employee employee = new Employee("Peter Burke", 39);
    Manager manager2 = new Manager("Jonathan Smith", 56);
    List<Employee> list = new ArrayList<Employee>();
    list.add(manager2);
    list.add(employee);
    list.add(manager);
    for (Employee emp : list) {
      emp.setManager(manager);
    }
    assertEquals(list, manager.getEmployees());
    assertEquals(manager, employee.getManager());
  }

  @Test
  public void testRoom() {
    Manager manager = new Manager();
    Room room = new Room();
    manager.setRoom(room);
    assertEquals(room, manager.getRoom());
    assertEquals(manager, room.getEmployees().get(0));
  }

  @Test
  public void testTeam() {
    Manager manager = new Manager();
    List<Manager> list = new ArrayList<Manager>();
    list.add(manager);
    Team team = new Team(TEAM_NAME);
    manager.setTeam(team);
    assertEquals(team, manager.getTeam());
    assertEquals(list, team.getEmployees());
  }

  @Test
  public void testEmployees() {
    Manager manager = new Manager();
    Employee employee1 = new Employee();
    Employee employee2 = new Employee();
    List<Employee> employeesList = new ArrayList<Employee>();
    employeesList.add(employee1);
    employeesList.add(employee2);
    for (Employee emp : employeesList) {
      emp.setManager(manager);
    }
    List<Employee> testList = manager.getEmployees();
    assertEquals(testList, employeesList);
    assertEquals(manager, employee1.getManager());
  }

}
