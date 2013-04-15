/**
 * (c) 2013 by SAP AG
 */
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
public class RoomTest extends BaseTest {

  private static final int VALUE_VERSION_NR = 1;
  private static final int VALUE_4 = 4;
  private static final String NAME = "Room 100";

  @Test
  public void testId() {
    Room room1 = new Room(1, NAME);
    assertNotNull(room1.getId());
  }

  @Test
  public void testSeats() {
    Room room1 = new Room(1, null);
    room1.setSeats(VALUE_4);
    assertEquals(VALUE_4, room1.getSeats());
  }

  @Test
  public void testVersion() {
    Room room1 = new Room(1, null);
    room1.setVersion(VALUE_VERSION_NR);
    assertEquals(VALUE_VERSION_NR, room1.getVersion());
  }

  @Test
  public void testBuilding() {
    Room room1 = new Room(1, null);
    Building build1 = new Building(1, null);
    build1.getRooms().add(room1);
    room1.setBuilding(build1);
    assertEquals(build1, room1.getBuilding());
    assertEquals(room1, build1.getRooms().get(0));
  }

  @Test
  public void testEmployees() {
    Employee employee1 = new Employee(1, null);
    Employee employee2 = new Employee(2, null);
    List<Employee> employeesList = Arrays.asList(employee1, employee2);
    Room room1 = new Room(1, null);
    room1.getEmployees().addAll(employeesList);
    employee1.setRoom(room1);
    employee2.setRoom(room1);
    assertEquals(employeesList, room1.getEmployees());
    assertEquals(room1, employee1.getRoom());
  }

}
