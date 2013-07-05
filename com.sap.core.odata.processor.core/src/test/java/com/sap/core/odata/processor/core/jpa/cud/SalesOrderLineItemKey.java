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

public class SalesOrderLineItemKey {

  private int soId;
  private int liId;

  public SalesOrderLineItemKey() {

  }

  public SalesOrderLineItemKey(final int soId, final int liId) {
    super();
    this.soId = soId;
    this.liId = liId;
  }

  public int getSoId() {
    return soId;
  }

  public void setSoId(final int soId) {
    this.soId = soId;
  }

  public int getLiId() {
    return liId;
  }

  public void setLiId(final int liId) {
    this.liId = liId;
  }

}
