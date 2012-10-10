package org.odata4j.examples.producer.jpa.northwind;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

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

@Entity
@Table(name = "Products")
public class Products implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ProductID")
  private Integer ProductID;
  @Basic(optional = false)
  @Column(name = "ProductName")
  private String ProductName;
  @Column(name = "QuantityPerUnit")
  private String QuantityPerUnit;
  @Column(name = "UnitPrice")
  private BigDecimal UnitPrice;
  @Column(name = "UnitsInStock")
  private Short UnitsInStock;
  @Column(name = "UnitsOnOrder")
  private Short UnitsOnOrder;
  @Column(name = "ReorderLevel")
  private Short ReorderLevel;
  @Basic(optional = false)
  @Column(name = "Discontinued")
  private boolean Discontinued;
  @JoinColumn(name = "SupplierID", referencedColumnName = "SupplierID")
  @ManyToOne
  private Suppliers Supplier;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "Product")
  private Collection<Order_Details> Order_Details;
  @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
  @ManyToOne
  private Categories Category;
  @Column(name = "CategoryID", insertable = false, updatable = false)
  private Integer CategoryID;
  @Column(name = "SupplierID", insertable = false, updatable = false)
  private Integer SupplierID;

  public Products() {}

  public Products(Integer productID) {
    this.ProductID = productID;
  }

  public Products(Integer productID, String productName, boolean discontinued) {
    this.ProductID = productID;
    this.ProductName = productName;
    this.Discontinued = discontinued;
  }

  public Integer getProductID() {
    return ProductID;
  }

  public void setProductID(Integer productID) {
    this.ProductID = productID;
  }

  public String getProductName() {
    return ProductName;
  }

  public void setProductName(String productName) {
    this.ProductName = productName;
  }

  public String getQuantityPerUnit() {
    return QuantityPerUnit;
  }

  public void setQuantityPerUnit(String quantityPerUnit) {
    this.QuantityPerUnit = quantityPerUnit;
  }

  public BigDecimal getUnitPrice() {
    return UnitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.UnitPrice = unitPrice;
  }

  public Short getUnitsInStock() {
    return UnitsInStock;
  }

  public void setUnitsInStock(Short unitsInStock) {
    this.UnitsInStock = unitsInStock;
  }

  public Short getUnitsOnOrder() {
    return UnitsOnOrder;
  }

  public void setUnitsOnOrder(Short unitsOnOrder) {
    this.UnitsOnOrder = unitsOnOrder;
  }

  public Short getReorderLevel() {
    return ReorderLevel;
  }

  public void setReorderLevel(Short reorderLevel) {
    this.ReorderLevel = reorderLevel;
  }

  public boolean getDiscontinued() {
    return Discontinued;
  }

  public void setDiscontinued(boolean discontinued) {
    this.Discontinued = discontinued;
  }

  public Collection<Order_Details> getOrderDetailsCollection() {
    return Order_Details;
  }

  public void setOrderDetailsCollection(
      Collection<Order_Details> orderDetailsCollection) {
    this.Order_Details = orderDetailsCollection;
  }

  public Suppliers getSupplier() {
    return Supplier;
  }

  public void setSupplier(Suppliers supplierID) {
    this.Supplier = supplierID;
  }

  public Categories getCategory() {
    return Category;
  }

  public void setCategory(Categories categoryID) {
    this.Category = categoryID;
  }

  public void setCategoryID(Integer categoryID) {
    CategoryID = categoryID;
  }

  public Integer getCategoryID() {
    return CategoryID;
  }

  public void setSupplierID(Integer supplierID) {
    SupplierID = supplierID;
  }

  public Integer getSupplierID() {
    return SupplierID;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (ProductID != null
        ? ProductID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Products)) {
      return false;
    }
    Products other = (Products) object;
    if ((this.ProductID == null && other.ProductID != null)
        || (this.ProductID != null && !this.ProductID
            .equals(other.ProductID))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.odata4j.examples.producer.model.Products[productID="
        + ProductID + "]";
  }
}
