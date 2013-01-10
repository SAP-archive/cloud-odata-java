package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author SAP AG
 */
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
    List<Employee> list = Arrays.asList(manager2, employee, manager);
    manager.setEmployees(list);
    for (Employee emp : list)
      emp.setManager(manager);
    assertEquals(list, manager.getEmployees());
    assertEquals(manager, employee.getManager());
  }

  @Test
  public void testRoom() {
    Employee manager = new Manager();
    Room room = new Room();
    room.setEmployees(Arrays.asList(manager));
    manager.setRoom(room);
    assertEquals(room, manager.getRoom());
    assertEquals(manager, room.getEmployees().get(0));
  }

  @Test
  public void testTeam() {
    Employee manager = new Manager();
    List<Employee> list = Arrays.asList(manager);
    Team team = new Team(TEAM_NAME);
    team.setEmployees(list);
    manager.setTeam(team);
    assertEquals(team, manager.getTeam());
    assertEquals(list, team.getEmployees());
  }

  @Test
  public void testEmployees() {
    Manager manager = new Manager();
    Employee employee1 = new Employee();
    Employee employee2 = new Employee();
    List<Employee> employeesList = Arrays.asList(employee1, employee2);
    manager.setEmployees(employeesList);
    for (Employee emp : employeesList)
      emp.setManager(manager);
    List<Employee> testList = manager.getEmployees();
    assertEquals(testList, employeesList);
    assertEquals(manager, employee1.getManager());
  }

}
