package com.sap.core.odata.processor.core.jpa.cud;

public class SalesOrderLineItem {

  private int price;

  public SalesOrderLineItem(final int price) {
    super();
    this.price = price;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(final int price) {
    this.price = price;
  }

}
