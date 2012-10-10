package org.odata4j.test.integration.expressions;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.UnsignedByte;

public class PojoWithAllTypes {

  private byte[] binary;
  private boolean boolean_;
  private UnsignedByte byte_;
  private byte sbyte;
  private LocalDateTime dateTime;
  private BigDecimal decimal;
  private double double_;
  private Guid guid;
  private short int16;
  private int int32;
  private long int64;
  private float single;
  private String string;
  private LocalTime time;
  private DateTime dateTimeOffset;

  public PojoWithAllTypes() {}

  public PojoWithAllTypes(byte[] binary, boolean boolean_, UnsignedByte byte_, byte sbyte, LocalDateTime dateTime, BigDecimal decimal,
      double double_, Guid guid, short int16, int int32, long int64, float single, String string, LocalTime time, DateTime dateTimeOffset) {
    this.binary = binary;
    this.boolean_ = boolean_;
    this.byte_ = byte_;
    this.sbyte = sbyte;
    this.dateTime = dateTime;
    this.decimal = decimal;
    this.double_ = double_;
    this.guid = guid;
    this.int16 = int16;
    this.int32 = int32;
    this.int64 = int64;
    this.single = single;
    this.time = time;
    this.string = string;
    this.dateTimeOffset = dateTimeOffset;
  }

  public byte[] getBinary() {
    return binary;
  }

  //public void setBinary(byte[] value) {
  //  binary = value;
  // }

  public boolean getBoolean() {
    return boolean_;
  }

  public void setBoolean(boolean value) {
    boolean_ = value;
  }

  public UnsignedByte getByte() {
    return byte_;
  }

  public void setByte(UnsignedByte value) {
    byte_ = value;
  }

  public byte getSByte() {
    return sbyte;
  }

  public void setSByte(byte value) {
    sbyte = value;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime value) {
    dateTime = value;
  }

  public BigDecimal getDecimal() {
    return decimal;
  }

  public void setDecimal(BigDecimal value) {
    decimal = value;
  }

  public double getDouble() {
    return double_;
  }

  public void setDouble(double value) {
    double_ = value;
  }

  public Guid getGuid() {
    return guid;
  }

  public void setGuid(Guid value) {
    guid = value;
  }

  public short getInt16() {
    return int16;
  }

  public void setInt16(short value) {
    int16 = value;
  }

  public int getInt32() {
    return int32;
  }

  public void setInt32(int value) {
    int32 = value;
  }

  public long getInt64() {
    return int64;
  }

  public void setInt64(long value) {
    int64 = value;
  }

  public float getSingle() {
    return single;
  }

  public void setSingle(float value) {
    single = value;
  }

  public String getString() {
    return string;
  }

  public void setString(String value) {
    string = value;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime value) {
    time = value;
  }

  public DateTime getDateTimeOffset() {
    return dateTimeOffset;
  }

  public void setDateTimeOffset(DateTime value) {
    dateTimeOffset = value;
  }
}