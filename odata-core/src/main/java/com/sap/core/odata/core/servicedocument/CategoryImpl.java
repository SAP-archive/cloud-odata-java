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

import com.sap.core.odata.api.servicedocument.Category;
import com.sap.core.odata.api.servicedocument.CommonAttributes;

public class CategoryImpl implements Category {
  private String scheme;
  private String term;
  private CommonAttributes commonAttributes;
  private String label;

  @Override
  public String getScheme() {
    return scheme;
  }

  @Override
  public String getTerm() {
    return term;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return commonAttributes;
  }

  @Override
  public String getLabel() {
    return label;
  }

  public CategoryImpl setScheme(final String scheme) {
    this.scheme = scheme;
    return this;
  }

  public CategoryImpl setTerm(final String term) {
    this.term = term;
    return this;
  }

  public CategoryImpl setCommonAttributes(final CommonAttributes commonAttribute) {
    commonAttributes = commonAttribute;
    return this;
  }

  public CategoryImpl setLabel(final String label) {
    this.label = label;
    return this;
  }
}
