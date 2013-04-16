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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author SAP AG
 */
public class Photo {
  private static final String RESOURCE = "/darth.jpg";
  private static byte[] defaultImage;

  private final int id;
  private String name;
  private String type = "image/jpeg";
  private String imageUrl = "http://localhost" + RESOURCE;
  private byte[] image = defaultImage;
  private String imageType = type;
  private byte[] binaryData;
  private String content;

  public Photo(final int id, final String name, final String type) {
    this.id = id;
    setName(name);
    setType(type);
  }

  static {
    try {
      InputStream instream = Photo.class.getResourceAsStream(RESOURCE);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      int b = 0;
      while ((b = instream.read()) != -1) {
        stream.write(b);
      }

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
    if (binaryData == null) {
      return null;
    } else {
      return binaryData.clone();
    }
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

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj
        || obj != null && getClass() == obj.getClass() && id == ((Photo) obj).id;
  }

  @Override
  public String toString() {
    return "{\"Id\":" + id + ","
        + "\"Name\":\"" + name + "\","
        + "\"Type\":\"" + type + "\","
        + "\"ImageUrl\":\"" + imageUrl + "\","
        + "\"Image\":\"" + Arrays.toString(image) + "\","
        + "\"ImageType\":\"" + imageType + "\","
        + "\"Content:\"" + content + "\","
        + "\"BinaryData\":\"" + Arrays.toString(binaryData) + "\"}";
  }
}
