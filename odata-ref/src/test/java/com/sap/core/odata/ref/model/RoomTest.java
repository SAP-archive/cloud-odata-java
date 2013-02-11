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
    Room room1 = new Room(1, NAME, VALUE_4);
    String roomId = room1.getId();
    assertNotNull(roomId);
  }

  @Test
  public void testSeats() {
    Room room1 = new Room(1);
    room1.setSeats(VALUE_4);
    int numberOfSeats = room1.getSeats();
    assertEquals(VALUE_4, numberOfSeats);
  }

  @Test
  public void testVersion() {
    Room room1 = new Room(1);
    room1.setVersion(VALUE_VERSION_NR);
    int roomVersion = room1.getVersion();
    assertEquals(roomVersion, VALUE_VERSION_NR);
  }

  @Test
  public void testBuilding() {
    Room room1 = new Room(1);
    Building build1 = new Building(1);
    build1.getRooms().add(room1);
    room1.setBuilding(build1);
    Building testBuild = room1.getBuilding();
    assertEquals(build1, testBuild);
    Room testRoom = build1.getRooms().get(0);
    assertEquals(room1, testRoom);
  }

  @Test
  public void testEmployees() {
    Employee employee1 = new Employee(1);
    Employee employee2 = new Employee(2);
    List<Employee> employeesList = Arrays.asList(employee1, employee2);
    Room room1 = new Room(1);
    room1.setEmployees(employeesList);
    employee1.setRoom(room1);
    employee2.setRoom(room1);
    List<Employee> testList = room1.getEmployees();
    assertEquals(testList, employeesList);
    Room testRoom = employee1.getRoom();
    assertEquals(room1, testRoom);
  }

}
