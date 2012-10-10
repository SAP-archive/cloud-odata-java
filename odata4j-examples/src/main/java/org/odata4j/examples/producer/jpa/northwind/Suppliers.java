package org.odata4j.examples.producer.jpa.northwind;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Suppliers")
public class Suppliers implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "SupplierID")
  private Integer SupplierID;
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
  @Lob
  @Column(name = "HomePage")
  private String HomePage;
  @OneToMany(mappedBy = "Supplier")
  private Collection<Products> Products;

  public Suppliers() {}

  public Suppliers(Integer supplierID) {
    this.SupplierID = supplierID;
  }

  public Suppliers(Integer supplierID, String companyName) {
    this.SupplierID = supplierID;
    this.CompanyName = companyName;
  }

  public Integer getSupplierID() {
    return SupplierID;
  }

  public void setSupplierID(Integer supplierID) {
    this.SupplierID = supplierID;
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

  public String getHomePage() {
    return HomePage;
  }

  public void setHomePage(String homePage) {
    this.HomePage = homePage;
  }

  public Collection<Products> getProductsCollection() {
    return Products;
  }

  public void setProductsCollection(Collection<Products> productsCollection) {
    this.Products = productsCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (SupplierID != null
        ? SupplierID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Suppliers)) {
      return false;
    }
    Suppliers other = (Suppliers) object;
    if ((this.SupplierID == null && other.SupplierID != null)
        || (this.SupplierID != null && !this.SupplierID
            .equals(other.SupplierID))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.odata4j.examples.producer.model.Suppliers[supplierID="
        + SupplierID + "]";
  }
}
