/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Container and initialization code for the data objects of the reference scenario.
 * @author SAP AG
 */
public class DataContainer {

  private static final String IMAGE_JPEG = "image/jpeg";

  private List<Employee> employees = new ArrayList<Employee>();
  private List<Team> teams = new ArrayList<Team>();
  private List<Room> rooms = new ArrayList<Room>();
  private List<Manager> managers = new ArrayList<Manager>();
  private List<Building> buildings = new ArrayList<Building>();
  private List<Photo> photos = new ArrayList<Photo>();
  private int employeeId = 0;
  private int teamId = 0;
  private int roomId = 0;
  private int buildingId = 0;
  private int photoId = 0;

  public void init() {
    // ------------- Teams ---------------
    Team team1 = createTeam();
    team1.setScrumTeam(false);
    teams.add(team1);

    Team team2 = createTeam();
    team2.setScrumTeam(true);
    teams.add(team2);

    Team team3 = createTeam();
    team3.setScrumTeam(false);
    teams.add(team3);

    // ------------- Buildings ---------------
    Building building1 = createBuilding();
    buildings.add(building1);

    Building building2 = createBuilding();
    buildings.add(building2);

    Building building3 = createBuilding();
    buildings.add(building3);

    // ------------- Rooms ---------------
    Room room1 = createRoom();
    room1.setSeats(1);
    room1.setBuilding(building1);
    building1.getRooms().add(room1);
    room1.setVersion(1);
    rooms.add(room1);

    Room room2 = createRoom();
    room2.setSeats(5);
    room2.setBuilding(building2);
    building2.getRooms().add(room2);
    room2.setVersion(2);
    rooms.add(room2);

    Room room3 = createRoom();
    room3.setSeats(2);
    room3.setBuilding(building2);
    building2.getRooms().add(room3);
    room3.setVersion(3);
    rooms.add(room3);

    for (int i = 4; i <= 103; i++) {
      Room roomN = createRoom();
      roomN.setSeats(4 + (i - 3) % 5);
      roomN.setBuilding(building3);
      building3.getRooms().add(roomN);
      roomN.setVersion(1);
      rooms.add(roomN);
    }

    // ------------- Employees and Managers ------------
    Manager emp1 = createManager();
    emp1.setEmployeeName("Walter Winter");
    emp1.setAge(52);
    emp1.setTeam(team1);
    team1.getEmployees().add(emp1);
    emp1.setRoom(room1);
    room1.getEmployees().add(emp1);
    emp1.setManager(emp1);
    emp1.getEmployees().add(emp1);
    emp1.setLocation(new Location("Germany", "69124", "Heidelberg"));
    emp1.setEntryDate(generateDate(1999, 1, 1));
    emp1.setImageUri("Employees('1')/$value");
    emp1.setImage("/male_1_WinterW.jpg");
    emp1.setImageType(IMAGE_JPEG);
    employees.add(emp1);
    managers.add(emp1);

    Employee emp2 = createEmployee();
    emp2.setEmployeeName("Frederic Fall");
    emp2.setAge(32);
    emp2.setTeam(team1);
    team1.getEmployees().add(emp2);
    emp2.setRoom(room2);
    room2.getEmployees().add(emp2);
    emp2.setManager(emp1);
    emp1.getEmployees().add(emp2);
    emp2.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp2.setEntryDate(generateDate(2003, 7, 1));
    emp2.setImageUri("Employees('2')/$value");
    emp2.setImage("/male_2_FallF.jpg");
    emp2.setImageType(IMAGE_JPEG);
    employees.add(emp2);

    Manager emp3 = createManager();
    emp3.setEmployeeName("Jonathan Smith");
    emp3.setAge(56);
    emp3.setTeam(team1);
    team1.getEmployees().add(emp3);
    emp3.setRoom(room2);
    room2.getEmployees().add(emp3);
    emp3.setManager(emp1);
    emp1.getEmployees().add(emp3);
    emp3.setLocation(emp2.getLocation());
    emp3.setEntryDate(null);
    emp3.setImageUri("Employees('3')/$value");
    emp3.setImage("/male_3_SmithJo.jpg");
    emp3.setImageType(IMAGE_JPEG);
    employees.add(emp3);
    managers.add(emp3);

    Employee emp4 = createEmployee();
    emp4.setEmployeeName("Peter Burke");
    emp4.setAge(39);
    emp4.setTeam(team2);
    team2.getEmployees().add(emp4);
    emp4.setRoom(room2);
    room2.getEmployees().add(emp4);
    emp4.setManager(emp3);
    emp3.getEmployees().add(emp4);
    emp4.setLocation(emp2.getLocation());
    emp4.setEntryDate(generateDate(2004, 9, 12));
    emp4.setImageUri("Employees('4')/$value");
    emp4.setImage("/male_4_BurkeP.jpg");
    emp4.setImageType(IMAGE_JPEG);
    employees.add(emp4);

    Employee emp5 = createEmployee();
    emp5.setEmployeeName("John Field");
    emp5.setAge(42);
    emp5.setTeam(team2);
    team2.getEmployees().add(emp5);
    emp5.setRoom(room3);
    room3.getEmployees().add(emp5);
    emp5.setManager(emp3);
    emp3.getEmployees().add(emp5);
    emp5.setLocation(emp2.getLocation());
    emp5.setEntryDate(generateDate(2001, 2, 1));
    emp5.setImageUri("Employees('5')/$value");
    emp5.setImage("/male_5_FieldJ.jpg");
    emp5.setImageType(IMAGE_JPEG);
    employees.add(emp5);

    Employee emp6 = createEmployee();
    emp6.setEmployeeName("Susan Bay");
    emp6.setAge(29);
    emp6.setTeam(team3);
    team3.getEmployees().add(emp6);
    emp6.setRoom(room2);
    room2.getEmployees().add(emp6);
    emp6.setManager(emp1);
    emp1.getEmployees().add(emp6);
    emp6.setLocation(emp2.getLocation());
    emp6.setEntryDate(generateDate(2010, 12, 1));
    emp6.setImageUri("Employees('6')/$value");
    emp6.setImage("/female_6_BaySu.jpg");
    emp6.setImageType(IMAGE_JPEG);
    employees.add(emp6);

    // ------------- Photos ---------------
    Photo photo1 = createPhoto("image/png");
    photo1.setContent("Образ");
    photos.add(photo1);

    Photo photo2 = createPhoto("image/bmp");
    photos.add(photo2);

    Photo photo3 = createPhoto(IMAGE_JPEG);
    photos.add(photo3);

    Photo photo4 = createPhoto("foo");
    photo4.setContent("Продукт");
    photos.add(photo4);
  }

  private Calendar generateDate(final int year, final int month, final int day) {
    Calendar date = Calendar.getInstance();

    date.clear();
    date.setTimeZone(TimeZone.getTimeZone("GMT"));
    date.set(year, month - 1, day); // month is zero-based!
    return date;
  }

  public Employee createEmployee() {
    return new Employee(++employeeId, "Employee " + employeeId);
  }

  public Team createTeam() {
    return new Team(++teamId, "Team " + teamId);
  }

  public Room createRoom() {
    return new Room(++roomId, "Room " + roomId);
  }

  public Manager createManager() {
    return new Manager(++employeeId, "Employee " + employeeId);
  }

  public Building createBuilding() {
    return new Building(++buildingId, "Building " + buildingId);
  }

  public Photo createPhoto(final String type) {
    return new Photo(++photoId, "Photo " + photoId, type);
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public List<Manager> getManagers() {
    return managers;
  }

  public List<Building> getBuildings() {
    return buildings;
  }

  public List<Photo> getPhotos() {
    return photos;
  }

  public void reset() {
    employees.clear();
    teams.clear();
    rooms.clear();
    managers.clear();
    buildings.clear();
    photos.clear();

    employeeId = 0;
    teamId = 0;
    roomId = 0;
    buildingId = 0;
    photoId = 0;

    init();
  }
}
