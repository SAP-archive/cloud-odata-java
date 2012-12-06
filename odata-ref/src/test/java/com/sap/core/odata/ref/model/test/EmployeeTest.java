package com.sap.core.odata.ref.model.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.junit.Test;

import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Location;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

public class EmployeeTest {

  private static final int VALUE_AGE = 36;
  private static final String VALUE_NAME = "Alex Kaiser";
  private static final String VALUE_URI = "http://localhost/employee1.jpg";
  private static final String IMAGE_URL = "/darth.jpg";
  private static final String IMAGE = "String for convert";
  private static final String TYPE = "image/jpeg";

  @Test
  public void testId() {
    Employee employee1 = new Employee();
    String employeeId = employee1.getId();
    assertNotNull(employeeId);
  }

  @Test
  public void testName() {
    Employee employee1 = new Employee();
    employee1.setEmployeeName(VALUE_NAME);
    String name = employee1.getEmployeeName();
    assertEquals(VALUE_NAME, name);
  }

  @Test
  public void testAge() {
    Employee employee1 = new Employee();
    employee1.setAge(VALUE_AGE);
    int employeeAge = employee1.getAge();
    assertEquals(employeeAge, VALUE_AGE);
  }

  @Test
  public void testImageUri() {
    Employee employee1 = new Employee();
    employee1.setImageUri(VALUE_URI);
    String testURI = employee1.getImageUri();
    assertEquals(testURI, VALUE_URI);
  }

  @Test
  public void testManager() {
    Employee employee1 = new Employee();
    Manager manager1 = new Manager();
    employee1.setManager(manager1);
    Manager testManager = employee1.getManager();
    assertEquals(manager1, testManager);
    assertEquals(employee1, manager1.getEmployees().get(0));
  }

  @Test
  public void testTeam() {
    Employee employee1 = new Employee();
    final Team team1 = new Team();
    employee1.setTeam(team1);
    Team testTeam = employee1.getTeam();
    assertEquals(team1, testTeam);
    assertEquals(employee1, team1.getEmployees().get(0));
  }

  @Test
  public void testRoom() {
    Employee employee1 = new Employee();
    Room room1 = new Room();
    employee1.setRoom(room1);
    Room testRoom = employee1.getRoom();
    assertEquals(room1, testRoom);
    assertEquals(employee1, room1.getEmployees().get(0));
  }

  @Test
  public void testLocation() {
    Location location = new Location();
    Employee emp1 = new Employee();
    emp1.setLocation(location);
    Location testLocation = emp1.getLocation();
    assertEquals(location, testLocation);
  }

  @Test
  public void testEntryDate() {
    Employee employee1 = new Employee();
    final Calendar date1 = Calendar.getInstance();
    employee1.setEntryDate(date1);
    Calendar testEntryDate = employee1.getEntryDate();
    assertEquals(date1, testEntryDate);
  }

  @Test
  public void testImage() {
    byte[] byteArray = null;
    Employee employee1 = new Employee();
    employee1.setEmployeeName(VALUE_NAME);
    employee1.setImage(IMAGE_URL);
    try {
      InputStream in = Employee.class.getResourceAsStream(IMAGE_URL);

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      int c = 0;
      while ((c = in.read()) != -1) {
        bos.write((char) c);
      }

      byteArray = bos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertArrayEquals(byteArray, employee1.getImage());
  }

  @Test
  public void testImage2() {
    byte[] byteArray = IMAGE.getBytes();
    Employee employee1 = new Employee();
    employee1.setImage(byteArray);
    byte[] byteArrayOfImage = employee1.getImage();
    assertEquals(byteArray.length, byteArrayOfImage.length);
    assertArrayEquals(byteArray, byteArrayOfImage);
  }

  @Test
  public void testImageType() {
    Employee employee1 = new Employee();
    employee1.setImageType(TYPE);
    String photoTyp = employee1.getImageType();
    assertEquals(photoTyp, TYPE);
  }

}
