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
package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * A {@link ReadEntryResult} represents an inlined navigation property which points to an entry.
 * The {@link ReadEntryResult} contains the {@link EntityProviderReadProperties} which were used for read, 
 * the <code>navigationPropertyName</code> and the read/de-serialized inlined entity.
 * If inlined navigation property is <code>nullable</code> the {@link ReadEntryResult} has the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * 
 * @author SAP AG
 *
 */
public class ReadEntryResult extends ReadResult {

  private final ODataEntry entry;

  /**
   * Constructor.
   * Parameters <b>MUST NOT BE NULL</b>.
   * 
   * @param properties read properties which are used to read enclosing parent entity
   * @param navigationProperty emd navigation property information of found inline navigation property
   * @param entry read entity as {@link ODataEntry}
   */
  public ReadEntryResult(final EntityProviderReadProperties properties, final EdmNavigationProperty navigationProperty, final ODataEntry entry) {
    super(properties, navigationProperty);
    this.entry = entry;
  }

  @Override
  public ODataEntry getResult() {
    return entry;
  }

  @Override
  public boolean isFeed() {
    return false;
  }

  @Override
  public String toString() {
    return super.toString() + "\n\t" + entry.toString();
  }
}
