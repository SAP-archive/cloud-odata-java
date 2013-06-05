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
package com.sap.core.odata.api.servicedocument;

import java.util.List;

/**
 * A AtomInfo
 * <p>AtomInfo represents the structure of Service Document according RFC 5023 (for ATOM format) 
 * @author SAP AG
 */
public interface AtomInfo {

  /**
   * Get the list of workspaces
   * 
   * @return a list of {@link Workspace}
   */
  public List<Workspace> getWorkspaces();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();

  /**
   * Get the list of extension elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getExtesionElements();
}
