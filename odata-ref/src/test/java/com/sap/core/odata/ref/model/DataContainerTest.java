package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class DataContainerTest extends BaseTest {

  private DataContainer dataContainer = new DataContainer();
  private static final String NAME = "Other name for team";

  @Test
  public void testReset() {
    dataContainer.init();
    List<Team> data = dataContainer.getTeams();
    for (Team team : data) {
      if (team.getId().equals("2")) {
        team.setName(NAME);
        assertEquals(team.getName(), NAME);
      }
    }
    dataContainer.reset();
    data = dataContainer.getTeams();
    for (Team team2 : data) {
      if (team2.getId() == "2") {
        assertEquals(team2.getName(), "Team 2");
        assertNotSame(team2.getName(), NAME);
      }
    }
  }

  @Test
  public void testReset2() {
    dataContainer.init();
    List<Team> data = dataContainer.getTeams();
    int initSize = data.size();
    Team team3 = new Team(4, "Testteam 4");
    data.add(team3);
    assertTrue(initSize != data.size());

    dataContainer.reset();
    data = dataContainer.getTeams();
    assertEquals(initSize, data.size());
  }

  @Test
  public void testInit() {
    dataContainer.init();
    assertFalse(dataContainer.getEmployees().isEmpty());
    assertFalse(dataContainer.getTeams().isEmpty());
    assertFalse(dataContainer.getRooms().isEmpty());
    assertFalse(dataContainer.getManagers().isEmpty());
    assertFalse(dataContainer.getBuildings().isEmpty());
    assertFalse(dataContainer.getPhotos().isEmpty());
  }

}
