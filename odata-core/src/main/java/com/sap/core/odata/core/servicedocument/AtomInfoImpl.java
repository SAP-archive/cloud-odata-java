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
package com.sap.core.odata.core.servicedocument;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.servicedocument.AtomInfo;
import com.sap.core.odata.api.servicedocument.Collection;
import com.sap.core.odata.api.servicedocument.CommonAttributes;
import com.sap.core.odata.api.servicedocument.ExtensionElement;
import com.sap.core.odata.api.servicedocument.Workspace;
import com.sap.core.odata.core.edm.provider.EdmEntitySetInfoImplProv;

/**
 * @author SAP AG
 */
public class AtomInfoImpl implements AtomInfo {
  private List<Workspace> workspaces;
  private CommonAttributes attributes;
  private List<ExtensionElement> extensionElements;

  @Override
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  public List<Workspace> getWorkspaces() {
    return workspaces;
  }

  @Override
  public List<ExtensionElement> getExtesionElements() {
    return extensionElements;
  }

  public AtomInfoImpl setWorkspaces(final List<Workspace> workspaces) {
    this.workspaces = workspaces;
    return this;
  }

  public AtomInfoImpl setCommonAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public AtomInfoImpl setExtesionElements(final List<ExtensionElement> elements) {
    extensionElements = elements;
    return this;
  }

  public List<EdmEntitySetInfo> getEntitySetsInfo() throws EntityProviderException {
    List<EdmEntitySetInfo> entitySets = new ArrayList<EdmEntitySetInfo>();
    for (Workspace workspace : workspaces) {
      for (Collection collection : workspace.getCollections()) {
        String[] names = collection.getHref().split("\\" + Edm.DELIMITER + "(?=[^" + Edm.DELIMITER + "]+$)");
        try {
          if (names.length == 1) {
            EntitySet entitySet = new EntitySet().setName(names[0]);
            EntityContainerInfo container = new EntityContainerInfo().setDefaultEntityContainer(true);
            EdmEntitySetInfo entitySetInfo = new EdmEntitySetInfoImplProv(entitySet, container);
            entitySets.add(entitySetInfo);
          } else if (names.length == 2) {
            EntitySet entitySet = new EntitySet().setName(names[1]);
            EntityContainerInfo container = new EntityContainerInfo().setName(names[0]).setDefaultEntityContainer(false);
            EdmEntitySetInfo entitySetInfo = new EdmEntitySetInfoImplProv(entitySet, container);
            entitySets.add(entitySetInfo);
          }
        } catch (EdmException e) {
          throw new EntityProviderException(EntityProviderException.COMMON, e);
        }
      }
    }
    return entitySets;
  }
}
