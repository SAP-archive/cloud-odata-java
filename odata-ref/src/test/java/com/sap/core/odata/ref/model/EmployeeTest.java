package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EmployeeTest extends BaseTest {

  private static final int VALUE_AGE = 36;
  private static final String VALUE_NAME = "Alex Kaiser";
  private static final String VALUE_URI = "http://localhost/employee1.jpg";
  private static final String IMAGE_URL = "/darth.jpg";
  private static final String IMAGE = "String for convert";
  private static final String TYPE = "image/jpeg";

  @Test
  public void testId() {
    Employee employee1 = new Employee(1, null);
    assertNotNull(employee1.getId());
  }

  @Test
  public void testName() {
    Employee employee1 = new Employee(1, VALUE_NAME);
    assertEquals(VALUE_NAME, employee1.getEmployeeName());
  }

  @Test
  public void testAge() {
    Employee employee1 = new Employee(1, null);
    employee1.setAge(VALUE_AGE);
    assertEquals(VALUE_AGE, employee1.getAge());
  }

  @Test
  public void testImageUri() {
    Employee employee1 = new Employee(1, null);
    employee1.setImageUri(VALUE_URI);
    assertEquals(VALUE_URI, employee1.getImageUri());
  }

  @Test
  public void testManager() {
    Employee employee1 = new Employee(1, null);
    Manager manager1 = new Manager(2, null);
    manager1.getEmployees().add(employee1);
    employee1.setManager(manager1);
    assertEquals(manager1, employee1.getManager());
    assertEquals(employee1, manager1.getEmployees().get(0));
  }

  @Test
  public void testTeam() {
    Employee employee1 = new Employee(1, null);
    final Team team1 = new Team(1, null);
    team1.getEmployees().add(employee1);
    employee1.setTeam(team1);
    assertEquals(team1, employee1.getTeam());
    assertEquals(employee1, team1.getEmployees().get(0));
  }

  @Test
  public void testRoom() {
    Employee employee1 = new Employee(1, null);
    Room room1 = new Room(1, null);
    room1.getEmployees().add(employee1);
    employee1.setRoom(room1);
    assertEquals(room1, employee1.getRoom());
    assertEquals(employee1, room1.getEmployees().get(0));
  }

  @Test
  public void testLocation() {
    Location location = new Location(null, null, null);
    Employee emp1 = new Employee(1, null);
    emp1.setLocation(location);
    assertEquals(location, emp1.getLocation());
  }

  @Test
  public void testEntryDate() {
    Employee employee1 = new Employee(1, null);
    final Calendar date1 = Calendar.getInstance();
    employee1.setEntryDate(date1);
    assertEquals(date1, employee1.getEntryDate());
  }

  @Test
  public void testImage() {
    byte[] byteArray = null;
    Employee employee1 = new Employee(1, VALUE_NAME);
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
    Employee employee1 = new Employee(1, null);
    employee1.setImage(byteArray);
    byte[] byteArrayOfImage = employee1.getImage();
    assertEquals(byteArray.length, byteArrayOfImage.length);
    assertArrayEquals(byteArray, byteArrayOfImage);
  }

  @Test
  public void testImageType() {
    Employee employee1 = new Employee(1, null);
    employee1.setImageType(TYPE);
    assertEquals(TYPE, employee1.getImageType());
  }

}
