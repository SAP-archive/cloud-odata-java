package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class TeamTest extends BaseTest {

  private static final String VALUE_NAME = "Team 1";

  @Test
  public void testId() {
    Team team1 = new Team(1, null);
    assertNotNull(team1.getId());
  }

  @Test
  public void testName() {
    Team team1 = new Team(1, VALUE_NAME);
    assertEquals(team1.getName(), VALUE_NAME);
  }

  @Test
  public void testIsScrumTeam() {
    Team team1 = new Team(1, null);
    team1.setScrumTeam(true);
    assertTrue(team1.isScrumTeam());
  }

  @Test
  public void testEmployees() {
    Team team1 = new Team(1, VALUE_NAME);
    Employee employee1 = new Employee(1, null);
    Employee employee2 = new Employee(2, null);
    List<Employee> testList = Arrays.asList(employee1, employee2);
    team1.getEmployees().addAll(testList);
    for (Employee emp : testList) {
      emp.setTeam(team1);
    }
    assertEquals(testList, team1.getEmployees());
    assertEquals(team1, employee1.getTeam());
  }
}
