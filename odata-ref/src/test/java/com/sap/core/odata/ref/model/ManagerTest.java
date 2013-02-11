package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ManagerTest extends BaseTest {

  private static final String VALUE_NAME = "Peter Schulz";
  private static final String TEAM_NAME = "Team 1";

  @Test
  public void testId() {
    Manager manager = new Manager(1);
    String testMngId = manager.getId();
    assertNotNull(testMngId);
  }

  @Test
  public void testManagerName() {
    Manager manager = new Manager(1);
    manager.setEmployeeName(VALUE_NAME);
    String testName = manager.getEmployeeName();
    assertEquals(VALUE_NAME, testName);
  }

  @Test
  public void testManager() {
    Manager manager = new Manager(1, "Walter Winter", 52);
    Employee employee = new Employee(2, "Peter Burke", 39);
    Manager manager2 = new Manager(3, "Jonathan Smith", 56);
    List<Employee> list = Arrays.asList(manager2, employee, manager);
    manager.setEmployees(list);
    for (Employee emp : list)
      emp.setManager(manager);
    assertEquals(list, manager.getEmployees());
    assertEquals(manager, employee.getManager());
  }

  @Test
  public void testRoom() {
    Employee manager = new Manager(1);
    Room room = new Room(1);
    room.setEmployees(Arrays.asList(manager));
    manager.setRoom(room);
    assertEquals(room, manager.getRoom());
    assertEquals(manager, room.getEmployees().get(0));
  }

  @Test
  public void testTeam() {
    Employee manager = new Manager(1);
    List<Employee> list = Arrays.asList(manager);
    Team team = new Team(1, TEAM_NAME);
    team.setEmployees(list);
    manager.setTeam(team);
    assertEquals(team, manager.getTeam());
    assertEquals(list, team.getEmployees());
  }

  @Test
  public void testEmployees() {
    Manager manager = new Manager(1);
    Employee employee1 = new Employee(2);
    Employee employee2 = new Employee(3);
    List<Employee> employeesList = Arrays.asList(employee1, employee2);
    manager.setEmployees(employeesList);
    for (Employee emp : employeesList)
      emp.setManager(manager);
    List<Employee> testList = manager.getEmployees();
    assertEquals(testList, employeesList);
    assertEquals(manager, employee1.getManager());
  }

}
