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
package com.sap.core.odata.core.rest.app;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.sap.core.odata.core.rest.ODataExceptionMapperImpl;
import com.sap.core.odata.core.rest.ODataRootLocator;

public class ODataApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(ODataRootLocator.class);
    classes.add(ODataExceptionMapperImpl.class);
    return classes;
  }

  /**
   * Singletons are not recommended because they break the state less REST principle.
   */
  @Override
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }

}
