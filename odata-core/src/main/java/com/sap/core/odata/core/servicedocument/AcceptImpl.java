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

import com.sap.core.odata.api.servicedocument.Accept;
import com.sap.core.odata.api.servicedocument.CommonAttributes;

public class AcceptImpl implements Accept {
  private String value;
  private CommonAttributes commonAttributes;

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return commonAttributes;
  }

  public AcceptImpl setText(final String text) {
    value = text;
    return this;
  }

  public AcceptImpl setCommonAttributes(final CommonAttributes commonAttributes) {
    this.commonAttributes = commonAttributes;
    return this;
  }
}
