package com.sap.core.odata.processor.core.jpa.mock.data;

import java.util.Calendar;
import java.util.List;

public class JPATypeMock {

  public static final String ENTITY_NAME = "JPATypeMock";
  public static final String PROPERTY_NAME_MINT = "mInt";
  public static final String PROPERTY_NAME_MSTRING = "mString";
  public static final String PROPERTY_NAME_MDATETIME = "mDateTime";
  public static final String PROPERTY_NAME_KEY = "key";

  public static final String NAVIGATION_PROPERTY_X = "mRelatedEntity";
  public static final String NAVIGATION_PROPERTY_XS = "mRelatedEntities";

  private JPATypeEmbeddableMock key;
  private int mInt;
  private String mString;
  private Calendar mDateTime;
  private JPARelatedTypeMock mRelatedEntity;
  private List<JPARelatedTypeMock> mRelatedEntities;

  public String getMString() {
    return mString;
  }

  public void setMString(String mString) {
    this.mString = mString;
  }

  public JPATypeEmbeddableMock getKey() {
    return key;
  }

  public void setKey(JPATypeEmbeddableMock key) {
    this.key = key;
  }

  public int getMInt() {
    return mInt;
  }

  public void setMInt(int mInt) {
    this.mInt = mInt;
  }

  public Calendar getMDateTime() {
    return mDateTime;
  }

  public void setMDateTime(Calendar mDateTime) {
    this.mDateTime = mDateTime;
  }

  public JPARelatedTypeMock getMRelatedEntity() {
    return mRelatedEntity;
  }

  public void setMRelatedEntity(JPARelatedTypeMock mRelatedEntity) {
    this.mRelatedEntity = mRelatedEntity;
  }

  public List<JPARelatedTypeMock> getMRelatedEntities() {
    return mRelatedEntities;
  }

  public void setMRelatedEntities(List<JPARelatedTypeMock> mRelatedEntities) {
    this.mRelatedEntities = mRelatedEntities;
  }

  public static class JPATypeEmbeddableMock {

    public static final String PROPERTY_NAME_MBOOLEAN = "mBoolean";
    public static final String PROPERTY_Y = "y";
    public static final String PROPERTY_Z = "z";

    private boolean mBoolean;
    private int y;
    private JPATypeEmbeddableMock2 z;

    public void setMBoolean(boolean x) {
      this.mBoolean = x;
    }

    public void setY(int y) {
      this.y = y;
    }

    public void setZ(JPATypeEmbeddableMock2 z) {
      this.z = z;
    }

    public boolean getMBoolean() {
      return this.mBoolean;
    }

    public int getY() {
      return this.y;
    }

    public JPATypeEmbeddableMock2 getZ() {
      return this.z;
    }

    public static class JPATypeEmbeddableMock2 {
      private int x;
      private int y;

      public void setX(int x) {
        this.x = x;
      }

      public int getX() {
        return this.x;
      }

      public int getY() {
        return this.y;
      }
    }

  }

  public static class JPARelatedTypeMock {
    public static final String ENTITY_NAME = "JPARelatedTypeMock";
    public static final String PROPERTY_NAME_MLONG = "mLong";
    public static final String PROPERTY_NAME_MDOUBLE = "mDouble";
    public static final String PROPERTY_NAME_MBYTE = "mByte";
    public static final String PROPERTY_NAME_MBYTEARRAY = "mByteArray";

    private long mLong;
    private double mDouble;
    private byte mByte;
    private byte mByteArray[];

    public long getMLong() {
      return mLong;
    }

    public void setMLong(long key) {
      this.mLong = key;
    }

    public double getMDouble() {
      return mDouble;
    }

    public void setMDouble(double mDouble) {
      this.mDouble = mDouble;
    }

    public byte getMByte() {
      return mByte;
    }

    public void setMByte(byte mByte) {
      this.mByte = mByte;
    }

    public byte[] getMByteArray() {
      return mByteArray;
    }

    public void setMByteArray(byte mByteArray[]) {
      this.mByteArray = mByteArray;
    }

  }
}
