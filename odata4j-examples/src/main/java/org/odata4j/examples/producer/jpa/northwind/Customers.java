package org.odata4j.examples.producer.jpa.northwind;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Customers")
public class Customers implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "CustomerID")
  private String CustomerID;
  @Basic(optional = false)
  @Column(name = "CompanyName")
  private String CompanyName;
  @Column(name = "ContactName")
  private String ContactName;
  @Column(name = "ContactTitle")
  private String ContactTitle;
  @Column(name = "Address")
  private String Address;
  @Column(name = "City")
  private String City;
  @Column(name = "Region")
  private String Region;
  @Column(name = "PostalCode")
  private String PostalCode;
  @Column(name = "Country")
  private String Country;
  @Column(name = "Phone")
  private String Phone;
  @Column(name = "Fax")
  private String Fax;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "Customer")
  private Collection<Orders> Orders;

  public Customers() {}

  public Customers(String customerID) {
    this.CustomerID = customerID;
  }

  public Customers(String customerID, String companyName) {
    this.CustomerID = customerID;
    this.CompanyName = companyName;
  }

  public String getCustomerID() {
    return CustomerID;
  }

  public void setCustomerID(String customerID) {
    this.CustomerID = customerID;
  }

  public String getCompanyName() {
    return CompanyName;
  }

  public void setCompanyName(String companyName) {
    this.CompanyName = companyName;
  }

  public String getContactName() {
    return ContactName;
  }

  public void setContactName(String contactName) {
    this.ContactName = contactName;
  }

  public String getContactTitle() {
    return ContactTitle;
  }

  public void setContactTitle(String contactTitle) {
    this.ContactTitle = contactTitle;
  }

  public String getAddress() {
    return Address;
  }

  public void setAddress(String address) {
    this.Address = address;
  }

  public String getCity() {
    return City;
  }

  public void setCity(String city) {
    this.City = city;
  }

  public String getRegion() {
    return Region;
  }

  public void setRegion(String region) {
    this.Region = region;
  }

  public String getPostalCode() {
    return PostalCode;
  }

  public void setPostalCode(String postalCode) {
    this.PostalCode = postalCode;
  }

  public String getCountry() {
    return Country;
  }

  public void setCountry(String country) {
    this.Country = country;
  }

  public String getPhone() {
    return Phone;
  }

  public void setPhone(String phone) {
    this.Phone = phone;
  }

  public String getFax() {
    return Fax;
  }

  public void setFax(String fax) {
    this.Fax = fax;
  }

  public Collection<Orders> getOrders() {
    return Orders;
  }

  public void setOrders(Collection<Orders> orders) {
    this.Orders = orders;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (CustomerID != null
        ? CustomerID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Customers)) {
      return false;
    }
    Customers other = (Customers) object;
    if ((this.CustomerID == null && other.CustomerID != null)
        || (this.CustomerID != null && !this.CustomerID
            .equals(other.CustomerID))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.odata4j.examples.producer.model.Customers[customerID="
        + CustomerID + "]";
  }

}
