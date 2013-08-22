package com.sap.core.odata.processor.core.jpa.mock.data;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/*========================================================================= */
public class JPATypeMock {

  public static final String ENTITY_NAME = "JPATypeMock";
  public static final String PROPERTY_NAME_MINT = "mInt";
  public static final String PROPERTY_NAME_MSTRING = "mString";
  public static final String PROPERTY_NAME_MDATETIME = "mDateTime";
  public static final String PROPERTY_NAME_MKEY = "key";
  public static final String PROPERTY_NAME_MCOMPLEXTYPE = "complexType";

  public static final String NAVIGATION_PROPERTY_X = "mRelatedEntity";
  public static final String NAVIGATION_PROPERTY_XS = "mRelatedEntities";

  private JPATypeEmbeddableMock key;
  private JPATypeEmbeddableMock complexType;
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

  public JPATypeEmbeddableMock getComplexType() {
    return complexType;
  }

  public void setComplexType(JPATypeEmbeddableMock complexType) {
    this.complexType = complexType;
  }

  /*========================================================================= */
  public static class JPATypeEmbeddableMock {

    public static final String ENTITY_NAME = "JPATypeEmbeddableMock";
    public static final String PROPERTY_NAME_MSHORT = "mShort";
    public static final String PROPERTY_NAME_MEMBEDDABLE = "mEmbeddable";

    private short mShort;
    private JPATypeEmbeddableMock2 mEmbeddable;

    public short getMShort() {
      return mShort;
    }

    public void setMShort(short mShort) {
      this.mShort = mShort;
    }

    public JPATypeEmbeddableMock2 getMEmbeddable() {
      return mEmbeddable;
    }

    public void setMEmbeddable(JPATypeEmbeddableMock2 mEmbeddable) {
      this.mEmbeddable = mEmbeddable;
    }

  }

  /*========================================================================= */
  public static class JPATypeEmbeddableMock2 {

    public static final String ENTITY_NAME = "JPATypeEmbeddableMock2";
    public static final String PROPERTY_NAME_MUUID = "mUUID";
    public static final String PROPERTY_NAME_MFLOAT = "mFloat";

    private UUID mUUID;
    private float mFloat;

    public UUID getMUUID() {
      return mUUID;
    }

    public void setMUUID(UUID mUUID) {
      this.mUUID = mUUID;
    }

    public float getMFloat() {
      return mFloat;
    }

    public void setMFloat(float mFloat) {
      this.mFloat = mFloat;
    }

  }

  /*========================================================================= */
  public static final class JPARelatedTypeMock {
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
