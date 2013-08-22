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
package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;

import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;

public class JPAEdmMappingImpl extends Mapping implements JPAEdmMapping {

  private String columnName = null;
  private Class<?> type = null;
  private ArrayList<Class<?>> typeHierarchy = null;

  @Override
  public void setJPAColumnName(final String name) {
    columnName = name;

  }

  @Override
  public String getJPAColumnName() {
    return columnName;
  }

  @Override
  public void setJPAType(final Class<?> type) {
    this.type = type;

  }

  @Override
  public Class<?> getJPAType() {
    return type;
  }

  @Override
  public Class<?>[] getJPATypeHierachy() {
    if (typeHierarchy != null)
      return (Class<?>[]) typeHierarchy.toArray();
    return null;
  }

  @Override
  public int addToJPATypeHierachy(Class<?> type) {
    if (typeHierarchy == null)
      typeHierarchy = new ArrayList<Class<?>>();

    typeHierarchy.add(type);
    return typeHierarchy.size();
  }

}
