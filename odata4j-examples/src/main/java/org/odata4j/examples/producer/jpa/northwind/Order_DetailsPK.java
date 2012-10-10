package org.odata4j.examples.producer.jpa.northwind;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Order_DetailsPK implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @Column(name = "OrderID")
  private int OrderID;
  @Basic(optional = false)
  @Column(name = "ProductID")
  private int ProductID;

  public Order_DetailsPK() {}

  public Order_DetailsPK(int orderID, int productID) {
    this.OrderID = orderID;
    this.ProductID = productID;
  }

  public int getOrderID() {
    return OrderID;
  }

  public void setOrderID(int orderID) {
    this.OrderID = orderID;
  }

  public int getProductID() {
    return ProductID;
  }

  public void setProductID(int productID) {
    this.ProductID = productID;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) OrderID;
    hash += (int) ProductID;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Order_DetailsPK)) {
      return false;
    }
    Order_DetailsPK other = (Order_DetailsPK) object;
    if (this.OrderID != other.OrderID) {
      return false;
    }
    if (this.ProductID != other.ProductID) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.odata4j.examples.producer.model.OrderDetailsPK[orderID="
        + OrderID + ", productID=" + ProductID + "]";
  }

}
