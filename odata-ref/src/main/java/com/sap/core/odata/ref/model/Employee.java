package com.sap.core.odata.ref.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Employee {
  private static int counter = 1;
  private int employeeId;
  private String employeeName;
  private int age;
  private Manager manager;
  private Team team;
  private Room room;
  private String imageType;
  private byte[] image;
  private String imageUrl;
  private Date entryDate;
  private Location location;

  public Employee() {
    this(null, 0);
  }

  public Employee(String name, int age) {
    employeeId = counter++;
    this.setEmployeeName(name);
    this.setAge(age);

  }

  public Employee(String name, int age, Room room, Team team) {
    this(name, age);
    this.setRoom(room);
    this.setTeam(team);
  }

  public String getId() {
    return Integer.toString(employeeId);
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;

  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setAge(int age) {
    this.age = age;

  }

  public int getAge() {
    return age;
  }

  public void setManager(Manager manager) {
    this.manager = manager;
    this.manager.getEmployees().add(this);
  }

  public Manager getManager() {
    return manager;

  }

  public void setTeam(Team team) {
    this.team = team;
    this.team.getEmployees().add(this);
  }

  public Team getTeam() {
    return team;
  }

  public void setRoom(Room room) {
    this.room = room;
    this.room.getEmployees().add(this);
  }

  public Room getRoom() {
    return room;
  }

  public void setImageUri(String imageUri) {
    this.imageUrl = imageUri;
  }

  public String getImageUri() {
    return imageUrl;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return location;
  }

  public void setEntryDate(Date date) {
    this.entryDate = date;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public void setImage(String imageUrl) {
    image = loadImage(imageUrl);
  }

  private byte[] loadImage(String imageUrl) {
    try {
      InputStream in = Employee.class.getResourceAsStream(imageUrl);
      // InputStream in = new BufferedInputStream(new FileInputStream(imageUrl));
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      int c = 0;
      while ((c = in.read()) != -1) {
        bos.write((char) c);
      }

      return bos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void reset() {
    counter = 1;
  }

  @Override
  public int hashCode() {
    return employeeId;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    if (employeeId != other.employeeId)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "{\"EmployeeId\":\"" + employeeId + "\","
        + "\"EmployeeName\":\"" + employeeName + "\","
        + "\"ManagerId\":\"" + manager.getId() + "\","
        + "\"RoomId\":\"" + room.getId() + "\","
        + "\"TeamId\":\"" + team.getId() + "\","
        + "\"Location\":{\"City\":{\"PostalCode\":\"" + location.getCity().getPostalCode() + "\","
        + "\"CityName\":\"" + location.getCity().getCityName() + "\"},"
        + "\"Country\":\"" + location.getCountry() + "\"},"
        + "\"Age\":" + age + ","
        + "\"EntryDate\":\"" + entryDate + "\","
        + "\"ImageUrl\":\"" + imageUrl + "\"}";
  }
}
