package com.sap.core.odata.processor.core.jpa.cud;

public class SalesOrderLineItemKey {

  private int soId;
  private int liId;

  public SalesOrderLineItemKey() {

  }

  public SalesOrderLineItemKey(final int soId, final int liId) {
    super();
    this.soId = soId;
    this.liId = liId;
  }

  public int getSoId() {
    return soId;
  }

  public void setSoId(final int soId) {
    this.soId = soId;
  }

  public int getLiId() {
    return liId;
  }

  public void setLiId(final int liId) {
    this.liId = liId;
  }

}
