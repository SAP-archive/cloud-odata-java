package org.odata4j.test.integration.expressions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.OStructuralObject;
import org.odata4j.core.UnsignedByte;

/**
 *
 */
public class PojoWithAllTypesComplex extends PojoWithAllTypes {

  private PojoWithAllTypes complexType;
  private List<String> stringList;
  private List<Complex1> complexes;
  private Entity1 favoriteEntity;
  private List<Entity1> onNoticeEntities;
  public boolean beforeUnmarshalCalled = false;
  public boolean afterUnmarshalCalled = false;

  public PojoWithAllTypesComplex() {}

  public PojoWithAllTypesComplex(byte[] binary, boolean boolean_, UnsignedByte byte_, byte sbyte, LocalDateTime dateTime, BigDecimal decimal,
      double double_, Guid guid, short int16, int int32, long int64, float single, String string, LocalTime time, DateTime dateTimeOffset,
      List<String> stringList, PojoWithAllTypes complexType) {

    super(binary, boolean_, byte_, sbyte, dateTime, decimal, double_, guid,
        int16, int32, int64, single, string, time, dateTimeOffset);
    this.complexType = complexType;
    this.stringList = stringList;
  }

  public PojoWithAllTypes getComplexType() {
    return this.complexType;
  }

  public void setComplexType(PojoWithAllTypes value) {
    complexType = value;
  }

  public List<String> getStringList() {
    return stringList;
  }

  public void setStringList(List<String> value) {
    stringList = value;
  }

  public Entity1 getFavoriteEntity() {
    return this.favoriteEntity;
  }

  public void setFavoriteEntity(Entity1 value) {
    this.favoriteEntity = value;
  }

  public List<Entity1> getOnNoticeEntities() {
    return this.onNoticeEntities;
  }

  public void setOnNoticeEntities(List<Entity1> value) {
    this.onNoticeEntities = value;
  }

  public void beforeOEntityUnmarshal(OStructuralObject sobj) {
    this.beforeUnmarshalCalled = true;
  }

  public void afterOEntityUnmarshal(OStructuralObject sobj) {
    this.afterUnmarshalCalled = true;
  }

  public static class Complex2 {

    public Complex2() {}

    public Complex2(String a, String b) {
      z1 = a;
      z2 = b;
    }

    public String getZ1() {
      return z1;
    }

    public void setZ1(String value) {
      z1 = value;
    }

    public String getZ2() {
      return z2;
    }

    public void setZ2(String value) {
      z2 = value;
    }

    private String z1;
    private String z2;
  }

  public static class Entity1 {
    public Entity1() {}

    public Entity1(String p1, int p2) {
      prop1 = p1;
      prop2 = p2;
    }

    public String getProp1() {
      return prop1;
    }

    public void setProp1(String value) {
      prop1 = value;
    }

    public int getProp2() {
      return prop2;
    }

    public void setProp2(int value) {
      prop2 = value;
    }

    private String prop1;
    private int prop2;
  }

  public static class Complex1 {

    public Complex1() {}

    public Complex1(String a, String b, Complex2 c2, List<Complex2> cc, List<String> cs) {
      s1 = a;
      s2 = b;
      this.embeddedComplex2 = c2;
      this.embeddedCollectionComplex2 = cc;
      this.embeddedCollectionString = cs;
    }

    public String getS1() {
      return s1;
    }

    public void setS1(String value) {
      s1 = value;
    }

    public String getS2() {
      return s2;
    }

    public void setS2(String value) {
      s2 = value;
    }

    public Complex2 getEmbeddedComplex2() {
      return this.embeddedComplex2;
    }

    public void setEmbeddedComplex2(Complex2 value) {
      this.embeddedComplex2 = value;
    }

    public List<Complex2> getEmbeddedCollectionComplex2() {
      return this.embeddedCollectionComplex2;
    }

    public void setEmbeddedCollectionComplex2(List<Complex2> value) {
      this.embeddedCollectionComplex2 = value;
    }

    public List<String> getEmbeddedCollectionString() {
      return this.embeddedCollectionString;
    }

    public void setEmbeddedCollectionString(List<String> value) {
      this.embeddedCollectionString = value;
    }

    private String s1;
    private String s2;
    private Complex2 embeddedComplex2;
    private List<Complex2> embeddedCollectionComplex2;
    private List<String> embeddedCollectionString;
  }

  public List<Complex1> getComplexes() {
    return complexes;
  }

  public void setComplexes(List<Complex1> value) {
    complexes = value;
  }

  public PojoWithAllTypesComplex addComplex1(Complex1 c) {
    if (complexes == null) {
      complexes = new ArrayList<Complex1>();
    }
    complexes.add(c);
    return this;
  }
}
