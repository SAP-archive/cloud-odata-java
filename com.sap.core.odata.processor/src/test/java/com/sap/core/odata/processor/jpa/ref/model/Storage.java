package com.sap.core.odata.processor.jpa.ref.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "T_STORAGE")
public class Storage {

	public Storage() {
		//No arg constructor
	}
	
	@EmbeddedId
	private StorageKey storageKey;
	
	@JoinColumn(name = "MATERIAL_ID", referencedColumnName = "MATERIAL_ID",insertable = false,updatable = false)
	@ManyToMany(mappedBy = "Storage")
	private List<Material> material = new ArrayList<Material>();
	
	@ManyToMany
	private List<Store> store = new ArrayList<Store>();
	
	public StorageKey getStorageKey() {
		return storageKey;
	}
	public void setStorageKey(StorageKey storageKey) {
		this.storageKey = storageKey;
	}
	
	public List<Material> getMaterial() {
		return material;
	}
	public void setMaterial(List<Material> material) {
		this.material = material;
		Iterator<Material> itr = material.iterator();
		while(itr.hasNext()) {
			itr.next().getStorage().add(this);
		}
	}
	
	public List<Store> getStore() {
		return store;
	}
	public void setStore(List<Store> store) {
		this.store = store;
		Iterator<Store> itr = store.iterator();
		while(itr.hasNext()) {
			itr.next().getStorage().add(this);
		}
	}
}