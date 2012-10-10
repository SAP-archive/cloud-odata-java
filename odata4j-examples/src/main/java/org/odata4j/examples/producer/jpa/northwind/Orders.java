package org.odata4j.examples.producer.jpa.northwind;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Orders")
public class Orders implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "OrderID")
  private Integer OrderID;
  @Column(name = "CustomerID", insertable = false, updatable = false)
  private String CustomerID;
  @Column(name = "EmployeeID", insertable = false, updatable = false)
  private Integer EmployeeID;
  @Column(name = "OrderDate")
  @Temporal(TemporalType.TIMESTAMP)
  private Date OrderDate;
  @Column(name = "RequiredDate")
  @Temporal(TemporalType.TIMESTAMP)
  private Date RequiredDate;
  @Column(name = "ShippedDate")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ShippedDate;
  @Column(name = "ShipVia")
  private Integer ShipVia;
  @Column(name = "Freight")
  private BigDecimal Freight;
  @Column(name = "ShipName")
  private String ShipName;
  @Column(name = "ShipAddress")
  private String ShipAddress;
  @Column(name = "ShipCity")
  private String ShipCity;
  @Column(name = "ShipRegion")
  private String ShipRegion;
  @Column(name = "ShipPostalCode")
  private String ShipPostalCode;
  @Column(name = "ShipCountry")
  private String ShipCountry;
  @JoinColumn(name = "CustomerID")
  @ManyToOne
  private Customers Customer;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "Order")
  private Collection<Order_Details> OrderDetails;
  @JoinColumn(name = "EmployeeID")
  @ManyToOne
  private Employees Employee;

  public Orders() {}

  public Orders(Integer orderID) {
    this.OrderID = orderID;
  }

  public Integer getOrderID() {
    return OrderID;
  }

  public void setOrderID(Integer orderID) {
    this.OrderID = orderID;
  }

  public String getCustomerID() {
    return CustomerID;
  }

  public void setCustomerID(String customerID) {
    this.CustomerID = customerID;
  }

  public Integer getEmployeeID() {
    return EmployeeID;
  }

  public void setEmployeeID(Integer employeeID) {
    this.EmployeeID = employeeID;
  }

  public Date getOrderDate() {
    return OrderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.OrderDate = orderDate;
  }

  public Date getRequiredDate() {
    return RequiredDate;
  }

  public void setRequiredDate(Date requiredDate) {
    this.RequiredDate = requiredDate;
  }

  public Date getShippedDate() {
    return ShippedDate;
  }

  public void setShippedDate(Date shippedDate) {
    this.ShippedDate = shippedDate;
  }

  public Integer getShipVia() {
    return ShipVia;
  }

  public void setShipVia(Integer shipVia) {
    this.ShipVia = shipVia;
  }

  public BigDecimal getFreight() {
    return Freight;
  }

  public void setFreight(BigDecimal freight) {
    this.Freight = freight;
  }

  public String getShipName() {
    return ShipName;
  }

  public void setShipName(String shipName) {
    this.ShipName = shipName;
  }

  public String getShipAddress() {
    return ShipAddress;
  }

  public void setShipAddress(String shipAddress) {
    this.ShipAddress = shipAddress;
  }

  public String getShipCity() {
    return ShipCity;
  }

  public void setShipCity(String shipCity) {
    this.ShipCity = shipCity;
  }

  public String getShipRegion() {
    return ShipRegion;
  }

  public void setShipRegion(String shipRegion) {
    this.ShipRegion = shipRegion;
  }

  public String getShipPostalCode() {
    return ShipPostalCode;
  }

  public void setShipPostalCode(String shipPostalCode) {
    this.ShipPostalCode = shipPostalCode;
  }

  public String getShipCountry() {
    return ShipCountry;
  }

  public void setShipCountry(String shipCountry) {
    this.ShipCountry = shipCountry;
  }

  public Customers getCustomers() {
    return Customer;
  }

  public void setCustomers(Customers customers) {
    this.Customer = customers;
  }

  public Collection<Order_Details> getOrderDetailsCollection() {
    return OrderDetails;
  }

  public void setOrderDetailsCollection(
      Collection<Order_Details> orderDetailsCollection) {
    this.OrderDetails = orderDetailsCollection;
  }

  public Employees getEmployees() {
    return Employee;
  }

  public void setEmployees(Employees employees) {
    this.Employee = employees;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (OrderID != null
        ? OrderID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Orders)) {
      return false;
    }
    Orders other = (Orders) object;
    if ((this.OrderID == null && other.OrderID != null)
        || (this.OrderID != null && !this.OrderID.equals(other.OrderID))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.odata4j.examples.producer.model.Orders[orderID=" + OrderID
        + "]";
  }

}
