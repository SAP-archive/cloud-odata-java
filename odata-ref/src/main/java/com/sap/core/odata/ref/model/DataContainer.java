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

  private Set<Photo> photoSet;
  private Set<Building> buildingSet = new HashSet<Building>();
  private Set<Team> teamSet = new HashSet<Team>();
  private Set<Room> roomSet = new HashSet<Room>();
  private Set<Employee> employeeSet = new HashSet<Employee>();
  private Set<Manager> managerSet = new HashSet<Manager>();

  public void init() {
    photoSet = generatePhotos();

    // ------------- Buildings ---------------
    Building building1 = new Building("Building 1");
    Building building2 = new Building("Building 2");
    Building building3 = new Building("Building 3");
    buildingSet.add(building1);
    buildingSet.add(building2);
    buildingSet.add(building3);

    // ------------- Teams ---------------
    Team team1 = new Team("Team 1", SCRUMTEAM_FALSE);
    Team team2 = new Team("Team 2", SCRUMTEAM_TRUE);
    Team team3 = new Team("Team 3", SCRUMTEAM_FALSE);
    teamSet.add(team1);
    teamSet.add(team2);
    teamSet.add(team3);

    // ------------- Rooms ---------------

    Room room1 = new Room("Room 1", 1);
    Room room2 = new Room("Room 2", 5);
    Room room3 = new Room("Room 3", 4);
    room1.setBuilding(building1);
    room2.setBuilding(building2);
    room3.setBuilding(building2);
    room1.setVersion(1);
    room2.setVersion(2);
    room3.setVersion(3);
    roomSet.add(room1);
    roomSet.add(room2);
    roomSet.add(room3);
    for (int i = 4; i <= 103; i++) {
      Room roomN = new Room("Room " + i, (4 + i) / 5);
      roomN.setBuilding(building3);
      roomN.setVersion(1);
      roomSet.add(roomN);
    }

    // ------------- Employees and Managers ------------
    Employee emp1 = new Manager("Walter Winter", 52, room1, team1);
    emp1.setManager((Manager) emp1);
    emp1.setLocation(new Location("Germany", "69124", "Heidelberg"));
    emp1.setEntryDate(generateDate(1999, 1, 1));
    emp1.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg");
    emp1.setImage("/male_1_WinterW.jpg");
    emp1.setImageType(IMAGE_JPEG);
    employeeSet.add(emp1);
    managerSet.add((Manager) emp1);

    Employee emp2 = new Employee("Frederic Fall", 32, room2, team1);
    emp2.setManager((Manager) emp1);
    emp2.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp2.setEntryDate(generateDate(2003, 7, 1));
    emp2.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_2_FallF.jpg");
    emp2.setImage("/male_2_FallF.jpg");
    emp2.setImageType(IMAGE_JPEG);
    employeeSet.add(emp2);

    Manager emp3 = new Manager("Jonathan Smith", 56, room2, team1);
    emp3.setManager((Manager) emp1);
    emp3.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp3.setEntryDate(null);
    emp3.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_3_SmithJo.jpg");
    emp3.setImage("/male_3_SmithJo.jpg");
    emp3.setImageType(IMAGE_JPEG);
    employeeSet.add(emp3);
    managerSet.add((Manager) emp3);

    Employee emp4 = new Employee("Peter Burke", 39, room2, team2);
    emp4.setManager(emp3);
    emp4.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp4.setEntryDate(generateDate(2004, 9, 12));
    emp4.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_4_BurkeP.jpg");
    emp4.setImage("/male_4_BurkeP.jpg");
    emp4.setImageType(IMAGE_JPEG);
    employeeSet.add(emp4);

    Employee emp5 = new Employee("John Field", 42, room3, team2);
    emp5.setManager(emp3);
    emp5.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp5.setEntryDate(generateDate(2001, 2, 1));
    emp5.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_5_FieldJ.jpg");
    emp5.setImage("/male_5_FieldJ.jpg");
    emp5.setImageType(IMAGE_JPEG);
    employeeSet.add(emp5);

    Employee emp6 = new Employee("Susan Bay", 29, room2, team3);
    emp6.setManager((Manager) emp1);
    emp6.setLocation(new Location("Germany", "69190", "Walldorf"));
    emp6.setEntryDate(generateDate(2010, 12, 1));
    emp6.setImageUri("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/female_6_BaySu.jpg");
    emp6.setImage("/female_6_BaySu.jpg");
    emp6.setImageType(IMAGE_JPEG);
    employeeSet.add(emp6);

  }

  private Calendar generateDate(final int year, final int month, final int day) {
    Calendar date = Calendar.getInstance();

    date.clear();
    date.setTimeZone(TimeZone.getTimeZone("GMT"));
    date.set(year, month - 1, day);  // month is zero-based!
    return date;
  }

  private Set<Photo> generatePhotos() {
    Set<Photo> photoSet = new HashSet<Photo>();

    Photo photo1 = new Photo("Photo 1");
    photo1.setType("image/png");
    photo1.setContent("Образ");
    photoSet.add(photo1);

    Photo photo2 = new Photo("Photo 2");
    photo2.setType("image/bmp");
    photoSet.add(photo2);

    Photo photo3 = new Photo("Photo 3");
    photo3.setType("image/jpeg");
    photoSet.add(photo3);

    Photo photo4 = new Photo("Photo 4");
    photo4.setType("foo");
    photo4.setContent("Продукт");
    photoSet.add(photo4);

    return photoSet;
  }

  public Set<Photo> getPhotoSet() {
    return photoSet;
  }

  public Set<Employee> getEmployeeSet() {
    return employeeSet;
  }

  public Set<Building> getBuildingSet() {
    return buildingSet;
  }

  public Set<Room> getRoomSet() {
    return roomSet;
  }

  public Set<Team> getTeamSet() {
    return teamSet;
  }

  public void reset() {
    if (photoSet != null) {
      photoSet.clear();
    }
    if (employeeSet != null) {
      employeeSet.clear();
    }

    if (buildingSet != null) {
      buildingSet.clear();
    }

    if (roomSet != null) {
      roomSet.clear();
    }

    if (teamSet != null) {
      teamSet.clear();
    }

    if (managerSet != null) {
      managerSet.clear();
    }
    Team.reset();
    Building.reset();
    Employee.reset();
    Room.reset();
    Photo.reset();
    init();
  }

  public Set<Manager> getManagerSet() {
    return managerSet;
  }
}
