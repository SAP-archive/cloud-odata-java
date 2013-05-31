package com.sap.core.odata.processor.core.jpa.cud;

public class SalesOrderLineItemKey {
	
	private int soId;
	private int liId;
	public SalesOrderLineItemKey(){
		
	}
	public SalesOrderLineItemKey(int soId, int liId) {
		super();
		this.soId = soId;
		this.liId = liId;
	}
	public int getSoId() {
		return soId;
	}
	public void setSoId(int soId) {
		this.soId = soId;
	}
	public int getLiId() {
		return liId;
	}
	public void setLiId(int liId) {
		this.liId = liId;
	}
	

}
