package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author SAP AG
 */
public class Building {
  private static int counter = 1;
  private int id;
  private String name;
  private byte[] image;
  private List<Room> rooms = new ArrayList<Room>();

  public Building() {
    this(null);
  }

  public Building(String name) {
    id = counter++;
    setName(name);
  }

  public String getId() {
    return Integer.toString(id);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setImage(byte[] byteArray) {
    image = byteArray;
  }

  public byte[] getImage() {
    if (image == null)
      return null;
    else
      return image.clone();
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public void setRooms(List<Room> listOfRooms) {
    this.rooms = listOfRooms;
  }

  public static void reset() {
    counter = 1;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    
    return (id == ((Building) obj).id);
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"Image\":\"" + Arrays.toString(image) + "\"}";
  }
}
