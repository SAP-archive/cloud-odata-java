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
package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmProperty;

/**
 * An item of a $select system query option.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface SelectItem {

  /**
   * <code>true</code> if select=*
   * @return <code>true</code> if select=*
   */
  public boolean isStar();

  /**
   * Gets the EDM property.
   * @return {@link EdmProperty} property of this select item
   */
  public EdmProperty getProperty();

  /**
   * Gets the navigation-property segments for this select item.
   * @return List of {@link NavigationPropertySegment} for this select item or Collection.EmptyList
   */
  public List<NavigationPropertySegment> getNavigationPropertySegments();

}
