package com.sap.core.odata.ref.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Photo {
  private static int counter = 1;
  private static final String RESOURCE = "/darth.jpg";
  private static byte[] defaultImage;

  private int id;
  private String name;
  private String type = "image/jpeg";
  private String imageUrl = "http://localhost" + RESOURCE;
  private byte[] image = defaultImage;
  private String imageType = type;
  private byte[] binaryData;
  private String content;

  public Photo() {
    this(null);
  }

  public Photo(final String name) {
    id = counter++;
    setName(name);
  }

  static {
    try {
      InputStream instream = Photo.class.getResourceAsStream(RESOURCE);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      int b = 0;
      while ((b = instream.read()) != -1)
        stream.write(b);

      Photo.defaultImage = stream.toByteArray();
    } catch (IOException e) {
      throw new ModelException(e);
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getImageUri() {
    return imageUrl;
  }

  public void setImageUri(final String uri) {
    imageUrl = uri;
  }

  public byte[] getImage() {
    return image.clone();
  }

  public void setImage(final byte[] image) {
    this.image = image;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(final String imageType) {
    this.imageType = imageType;
  }

  public byte[] getBinaryData() {
    if (binaryData == null)
      return null;
    else
      return binaryData.clone();
  }

  public void setBinaryData(final byte[] binaryData) {
    this.binaryData = binaryData;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
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

    return id == ((Photo) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":" + id + ","
        + "\"Name\":\"" + name + "\","
        + "\"Type\":\"" + type + "\","
        + "\"ImageUrl\":\"" + imageUrl + "\","
        + "\"Image\":\"" + Arrays.toString(image) + "\","
        + "\"BinaryData\":\"" + Arrays.toString(binaryData) + "\"}";
  }
}
