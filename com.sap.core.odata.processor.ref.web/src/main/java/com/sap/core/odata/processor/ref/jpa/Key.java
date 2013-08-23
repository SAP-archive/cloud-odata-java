package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Key {
  
  @Column
  private int x;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }
  
  
}
