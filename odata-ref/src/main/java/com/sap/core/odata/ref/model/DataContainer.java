package com.sap.core.odata.ref.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author SAP AG
 */
public class DataContainer {

  private static final boolean SCRUMTEAM_TRUE = true;
  private static final boolean SCRUMTEAM_FALSE = false;
  private static final String IMAGE_JPEG = "image/jpeg";

  private Set<Employee> employeeSet = new HashSet<Employee>();
  private Set<Team> teamSet = new HashSet<Team>();
  private Set<Room> roomSet = new HashSet<Room>();
  private Set<Manager> managerSet = new HashSet<Manager>();
  private Set<Building> buildingSet = new HashSet<Building>();
  private Set<Photo> photoSet;
  
  private int photoId = 1;
  private int teamId = 1;
  private int buildingId = 1;
  private int employeeId = 1;
  private int roomId = 1;

  public void init() {
    // ------------- Teams ---------------
    Team team1 = createTeam("Team 1", SCRUMTEAM_FALSE);
    Team team2 = createTeam("Team 2", SCRUMTEAM_TRUE);
    Team team3 = createTeam("Team 3", SCRUMTEAM_FALSE);
    teamSet.add(team1);
    teamSet.add(team2);
    teamSet.add(team3);

    // ------------- Buildings ---------------
    Building building1 = createBuilding("Building 1");
    Building building2 = createBuilding("Building 2");
    Building building3 = createBuilding("Building 3");
    buildingSet.add(building1);
    buildingSet.add(building2);
    buildingSet.add(building3);

    // ------------- Rooms ---------------
    Room room1 = createRoom("Room 1", 1);
    Room room2 = createRoom("Room 2", 5);
    Room room3 = createRoom("Room 3", 4);
    room1.setBuilding(building1);
    building1.getRooms().add(room1);
    room2.setBuilding(building2);
    building2.getRooms().add(room2);
    room3.setBuilding(building2);
    building2.getRooms().add(room3);
    room1.setVersion(1);
    room2.setVersion(2);
    room3.setVersion(3);
    roomSet.add(room1);
    roomSet.add(room2);
    roomSet.add(room3);
    for (int i = 4; i <= 103; i++) {
      Room roomN = createRoom("Room " + i, 4 + (i - 3) % 5);
      roomN.setBuilding(building3);
      building3.getRooms().add(roomN);
      roomN.setVersion(1);
      roomSet.add(roomN);
    }

    // ------------- Employees and Managers ------------
    Manager emp1 = createManager("Walter Winter", 52, room1, team1);
    emp1.setManager(emp1);
    emp1.getEmployees().add(emp1);
    team1.getEmployees().add(emp1);
    room1.getEmployees().add(emp1);
    emp1.setLocation(new Location("Germany", "69124", "Heidelberg"));
    emp1.setEntryDate(generateDate(1999, 1, 1));
    emp1.setImageUri("Employees('1')/$value");
    emp1.setImage("/male_1_WinterW.jpg");
    emp1.setImageType(IMAGE_JPEG);
    employeeSet.add(emp1);
    managerSet.add(emp1);

    Employee emp2 = createEmployee("Frederic Fall", 32, room2, team1);
    emp2.setManager(emp1);
    emp1.getEmployees().add(emp2);
    team1.getEmployees().add(emp2);
    room2.getEmployees().add(emp2);
    emp2.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp2.setEntryDate(generateDate(2003, 7, 1));
    emp2.setImageUri("Employees('2')/$value");
    emp2.setImage("/male_2_FallF.jpg");
    emp2.setImageType(IMAGE_JPEG);
    employeeSet.add(emp2);

    Manager emp3 = createManager("Jonathan Smith", 56, room2, team1);
    emp3.setManager(emp1);
    emp1.getEmployees().add(emp3);
    team1.getEmployees().add(emp3);
    room2.getEmployees().add(emp3);
    emp3.setLocation(emp2.getLocation());
    emp3.setEntryDate(null);
    emp3.setImageUri("Employees('3')/$value");
    emp3.setImage("/male_3_SmithJo.jpg");
    emp3.setImageType(IMAGE_JPEG);
    employeeSet.add(emp3);
    managerSet.add(emp3);

    Employee emp4 = createEmployee("Peter Burke", 39, room2, team2);
    emp4.setManager(emp3);
    emp3.getEmployees().add(emp4);
    team2.getEmployees().add(emp4);
    room2.getEmployees().add(emp4);
    emp4.setLocation(emp2.getLocation());
    emp4.setEntryDate(generateDate(2004, 9, 12));
    emp4.setImageUri("Employees('4')/$value");
    emp4.setImage("/male_4_BurkeP.jpg");
    emp4.setImageType(IMAGE_JPEG);
    employeeSet.add(emp4);

    Employee emp5 = createEmployee("John Field", 42, room3, team2);
    emp5.setManager(emp3);
    emp3.getEmployees().add(emp5);
    team2.getEmployees().add(emp5);
    room3.getEmployees().add(emp5);
    emp5.setLocation(emp2.getLocation());
    emp5.setEntryDate(generateDate(2001, 2, 1));
    emp5.setImageUri("Employees('5')/$value");
    emp5.setImage("/male_5_FieldJ.jpg");
    emp5.setImageType(IMAGE_JPEG);
    employeeSet.add(emp5);

    Employee emp6 = createEmployee("Susan Bay", 29, room2, team3);
    emp6.setManager(emp1);
    emp1.getEmployees().add(emp6);
    team3.getEmployees().add(emp6);
    room2.getEmployees().add(emp6);
    emp6.setLocation(emp2.getLocation());
    emp6.setEntryDate(generateDate(2010, 12, 1));
    emp6.setImageUri("Employees('6')/$value");
    emp6.setImage("/female_6_BaySu.jpg");
    emp6.setImageType(IMAGE_JPEG);
    employeeSet.add(emp6);

    // ------------- Photos ---------------
    photoSet = generatePhotos();
  }

  private Calendar generateDate(final int year, final int month, final int day) {
    Calendar date = Calendar.getInstance();

    date.clear();
    date.setTimeZone(TimeZone.getTimeZone("GMT"));
    date.set(year, month - 1, day); // month is zero-based!
    return date;
  }

  private Set<Photo> generatePhotos() {
    Set<Photo> photoSet = new HashSet<Photo>();

    Photo photo1 = createPhoto("Photo 1");
    photo1.setType("image/png");
    photo1.setContent("Образ");
    photoSet.add(photo1);

    Photo photo2 = createPhoto("Photo 2");
    photo2.setType("image/bmp");
    photoSet.add(photo2);

    Photo photo3 = createPhoto("Photo 3");
    photo3.setType(IMAGE_JPEG);
    photoSet.add(photo3);

    Photo photo4 = createPhoto("Photo 4");
    photo4.setType("foo");
    photo4.setContent("Продукт");
    photoSet.add(photo4);

    return photoSet;
  }

  private Manager createManager(String name, int age, Room room, Team team) {
    return new Manager(employeeId++, name, age, room, team);
  }

  private Room createRoom(String name, int seats) {
    return new Room(roomId++, name, seats);
  }

  private Employee createEmployee(String name, int age, Room room, Team team) {
    return new Employee(employeeId++, name, age, room, team);
  }

  private Building createBuilding(String name) {
    return new Building(buildingId++, name);
  }

  private Team createTeam(String name, boolean isScrumTeam) {
    return new Team(teamId++, name, isScrumTeam);
  }

  private Photo createPhoto(String name) {
    return new Photo(photoId++, name);
  }

  public Photo createPhoto() {
    return new Photo(photoId++);
  }

  public Set<Employee> getEmployeeSet() {
    return employeeSet;
  }

  public Set<Team> getTeamSet() {
    return teamSet;
  }

  public Set<Room> getRoomSet() {
    return roomSet;
  }

  public Set<Manager> getManagerSet() {
    return managerSet;
  }

  public Set<Building> getBuildingSet() {
    return buildingSet;
  }

  public Set<Photo> getPhotoSet() {
    return photoSet;
  }

  /**
   */
  public void reset() {
    if (employeeSet != null)
      employeeSet.clear();
    if (teamSet != null)
      teamSet.clear();
    if (roomSet != null)
      roomSet.clear();
    if (managerSet != null)
      managerSet.clear();
    if (buildingSet != null)
      buildingSet.clear();
    if (photoSet != null)
      photoSet.clear();

    employeeId = 1;
    teamId = 1;
    buildingId = 1;
    roomId = 1;
    photoId = 1;
    
    init();
  }

  public Building createBuilding() {
    return new Building(buildingId++);
  }

  public Manager createManager() {
    return new Manager(employeeId++);
  }

  public Room createRoom() {
    return new Room(roomId++);
  }

  public Team createTeam() {
    return new Team(teamId++);
  }

  public Employee createEmployee() {
    return new Employee(employeeId++);
  }
}
