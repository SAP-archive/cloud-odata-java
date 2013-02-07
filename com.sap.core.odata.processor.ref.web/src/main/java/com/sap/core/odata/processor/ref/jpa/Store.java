package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "T_STORE")
public class Store {

	public Store() {
		//No arg constructor
	}
	
	public Store(String storeName, Address storeAddress) {
		super();
		this.storeName = storeName;
		this.storeAddress = storeAddress;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STORE_ID")
	private long storeId;
	
	@Column(name = "STORE_NAME", unique = true)
	private String storeName;
	
	@Embedded
	private Address storeAddress;
	
	@ManyToMany(mappedBy = "stores")
	private List<Material> materials = new ArrayList<Material>();

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Address getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(Address storeAddress) {
		this.storeAddress = storeAddress;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
		Iterator<Material> itr = materials.iterator();
		while(itr.hasNext()) {
			itr.next().getStores().add(this);
		}
	}	
}