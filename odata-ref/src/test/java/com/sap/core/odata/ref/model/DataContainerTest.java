package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

public class DataContainerTest extends BaseTest {
  
  private DataContainer dc = new DataContainer();
  private static final String NAME = "Other name for team";

  @Test
  public void testReset() {
    dc.init();
    Set<Team> datenSet = dc.getTeamSet();
    for (Team team : datenSet) {
      if (team.getId().equals("2")) {
        team.setName(NAME);
        assertEquals(team.getName(), NAME);
      }
    }
    dc.reset();
    datenSet = dc.getTeamSet();
    for (Team team2 : datenSet) {
      if (team2.getId() == "2") {
        assertEquals(team2.getName(), "Team 2");
        assertNotSame(team2.getName(), NAME);
      }
    }
  }

  @Test
  public void testReset2() {
    dc.init();
    Set<Team> datenSet = dc.getTeamSet();
    int initSetSize = datenSet.size();
    Team team3 = new Team("Testteam 4", false);
    datenSet.add(team3);
    assertNotSame(initSetSize, datenSet.size());

    dc.reset();
    datenSet = dc.getTeamSet();
    assertSame(initSetSize, datenSet.size());
  }

  @Test
  public void testInit() {
    dc.init();
    Set<?> set = dc.getEmployeeSet();
    assertTrue(!set.isEmpty());

    set = dc.getPhotoSet();
    assertTrue(!set.isEmpty());

    set = dc.getBuildingSet();
    assertTrue(!set.isEmpty());

    set = dc.getRoomSet();
    assertTrue(!set.isEmpty());

    set = dc.getTeamSet();
    assertTrue(!set.isEmpty());
  }

}
