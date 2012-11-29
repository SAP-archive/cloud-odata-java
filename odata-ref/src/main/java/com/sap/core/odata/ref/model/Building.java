package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    return image;
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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Building other = (Building) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "{\"Id\":\"" + id + "\",\"Name\":\"" + name + "\",\"Image\":\"" + Arrays.toString(image) + "\"}";
  }
}
