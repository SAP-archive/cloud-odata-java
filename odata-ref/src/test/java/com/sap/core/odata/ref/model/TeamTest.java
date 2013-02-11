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
public class TeamTest extends BaseTest {

  private static final String VALUE_NAME = "Team 1";
  private static final boolean SCRUM_TEAM_TRUE = true;

  @Test
  public void testId() {
    Team team1 = new Team(1);
    String testId = team1.getId();
    assertNotNull(testId);
  }

  @Test
  public void testName() {
    Team team1 = new Team(1);
    team1.setName(VALUE_NAME);
    String testName = team1.getName();
    assertEquals(testName, VALUE_NAME);
  }

  @Test
  public void testIsScrumTeam() {
    Team team1 = new Team(1);
    team1.setScrumTeam(SCRUM_TEAM_TRUE);
    boolean testValue = team1.isScrumTeam();
    assertEquals(testValue, SCRUM_TEAM_TRUE);
  }

  @Test
  public void testEmployees() {
    Team team1 = new Team(1, VALUE_NAME);
    Employee employee1 = new Employee(1);
    Employee employee2 = new Employee(2);
    List<Employee> testList = Arrays.asList(employee1, employee2);
    team1.setEmployees(testList);
    for (Employee emp : testList)
      emp.setTeam(team1);
    assertEquals(testList, team1.getEmployees());
    assertEquals(team1, employee1.getTeam());
  }
}
