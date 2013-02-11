package com.sap.core.odata.ref.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author SAP AG
 */
public class Building {
  private final int id;
  private String name;
  private byte[] image;
  private List<Room> rooms = new ArrayList<Room>();

  public Building(int id) {
    this(id, null);
  }

  public Building(int id, final String name) {
    this.id = id;
    setName(name);
  }

  public String getId() {
    return Integer.toString(id);
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setImage(final byte[] byteArray) {
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

  public void setRooms(final List<Room> listOfRooms) {
    this.rooms = listOfRooms;
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
