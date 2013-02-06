package com.sap.core.odata.processor.fit.ref.jpa;

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
	
	@JoinColumn(name = "STORE_NAME", referencedColumnName = "STORE_NAME",insertable = false,updatable = false)
	@ManyToMany(mappedBy = "Store",cascade = CascadeType.ALL)
	private List<Storage> storage = new ArrayList<Storage>();

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

	public List<Storage> getStorage() {
		return storage;
	}

	public void setStorage(List<Storage> storage) {
		this.storage = storage;
		Iterator<Storage> itr = storage.iterator();
		while(itr.hasNext()) {
			itr.next().getStore().add(this);
		}
	}	
}