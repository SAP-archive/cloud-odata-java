package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.*;

@Entity
@Table(name = "T_LINEITEMS")
public class LineItems {

	public LineItems() {
		super();
		//No arg const.
	}

	public LineItems(String name) {
		this();
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LINE_ITEM_ID")
	private long liId;

	@Column(name = "NAME")
	private String name;

	@JoinColumn(name = "SO_LINE_ITEM_ID", referencedColumnName = "SO_ID")
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public long getLiId() {
		return liId;
	}

	public void setLiId(long liId) {
		this.liId = liId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SalesOrderHeader getSalesOrderHeader() {
		return this.salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		this.salesOrderHeader.getLineItems().add(this);
	}

}
