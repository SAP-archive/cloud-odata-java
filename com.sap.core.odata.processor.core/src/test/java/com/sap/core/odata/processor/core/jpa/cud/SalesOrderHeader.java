package com.sap.core.odata.processor.core.jpa.cud;

import java.util.ArrayList;
import java.util.List;


public class SalesOrderHeader {
	
		private int id;
		private String description;
		
		public SalesOrderHeader()
		{}
		public SalesOrderHeader(int id,String description) {
			super();
			this.id = id;
			this.description = description;
		}
		private List<SalesOrderLineItem> salesOrderLineItems = new ArrayList<SalesOrderLineItem>();
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		public List<SalesOrderLineItem> getSalesOrderLineItems() {
			return salesOrderLineItems;
		}
		public void setSalesOrderLineItems(List<SalesOrderLineItem> salesOrderLineItems) {
			this.salesOrderLineItems = salesOrderLineItems;
		}
	

}
