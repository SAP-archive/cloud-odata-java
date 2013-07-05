/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.cud;

import java.util.ArrayList;
import java.util.List;

public class SalesOrderHeader {

  private int id;
  private String description;

  public SalesOrderHeader()
  {}

  public SalesOrderHeader(final int id, final String description) {
    super();
    this.id = id;
    this.description = description;
  }

  private List<SalesOrderLineItem> salesOrderLineItems = new ArrayList<SalesOrderLineItem>();

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public List<SalesOrderLineItem> getSalesOrderLineItems() {
    return salesOrderLineItems;
  }

  public void setSalesOrderLineItems(final List<SalesOrderLineItem> salesOrderLineItems) {
    this.salesOrderLineItems = salesOrderLineItems;
  }

}
