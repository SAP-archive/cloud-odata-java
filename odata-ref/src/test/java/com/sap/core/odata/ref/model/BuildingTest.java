package com.sap.core.odata.ref.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

public class BuildingTest extends BaseTest {

  private static final String VALUE_NAME = "bd01";

  private static final String VALUE_IMAGE = "String for convert";

  @Test
  public void testId() {
    Building build1 = new Building(1);
    String buildId = build1.getId();
    assertNotNull(buildId);
  }

  @Test
  public void testName() {
    Building build1 = new Building(1);
    // instanceCounter++;
    build1.setName(VALUE_NAME);
    String buildName = build1.getName();
    assertEquals(VALUE_NAME, buildName);
  }

  @Test
  public void testImage() {
    Building build1 = new Building(1);
    final byte[] byteArray = VALUE_IMAGE.getBytes();
    build1.setImage(byteArray);
    byte[] testArray = build1.getImage();
    assertEquals(byteArray.length, testArray.length);
    assertArrayEquals(byteArray, testArray);
  }

  @Test
  public void testRooms() {
    List<Room> list = new ArrayList<Room>();
    list.add(new Room(1));
    list.add(new Room(2));
    list.add(new Room(3));
    Building building1 = new Building(1);
    building1.setRooms(list);
    List<Room> testList = building1.getRooms();
    assertEquals(list, testList);
  }

}
