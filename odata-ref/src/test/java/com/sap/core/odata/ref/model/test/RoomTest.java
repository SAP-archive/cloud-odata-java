package com.sap.core.odata.ref.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.ref.model.Building;
import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Room;

public class RoomTest {

  private static final int VALUE_VERSION_NR = 1;
  private static final int VALUE_4 = 4;
  private static final String NAME = "Room 100";

  @Test
  public void testId() {
    Room room1 = new Room(NAME, VALUE_4);
    String roomId = room1.getId();
    assertNotNull(roomId);
  }

  @Test
  public void testSeats() {
    Room room1 = new Room();
    room1.setSeats(VALUE_4);
    int numberOfSeats = room1.getSeats();
    assertEquals(VALUE_4, numberOfSeats);
  }

  @Test
  public void testVersion() {
    Room room1 = new Room();
    room1.setVersion(VALUE_VERSION_NR);
    int roomVersion = room1.getVersion();
    assertEquals(roomVersion, VALUE_VERSION_NR);
  }

  @Test
  public void testBuilding() {
    Room room1 = new Room();
    Building build1 = new Building();
    room1.setBuilding(build1);
    Building testBuild = room1.getBuilding();
    assertEquals(build1, testBuild);
    Room testRoom = build1.getRooms().get(0);
    assertEquals(room1, testRoom);
  }

  @Test
  public void testEmployees() {
    Employee employee1 = new Employee();
    Employee employee2 = new Employee();
    List<Employee> employeesList = new ArrayList<Employee>();
    employeesList.add(employee1);
    employeesList.add(employee2);
    // Relationship should be set by n site
    Room room1 = new Room();
    employee1.setRoom(room1);
    employee2.setRoom(room1);
    List<Employee> testList = room1.getEmployees();
    assertEquals(testList, employeesList);
    Room testRoom = employee1.getRoom();
    assertEquals(room1, testRoom);
  }

}
