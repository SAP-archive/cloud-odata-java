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

import com.sap.core.odata.api.servicedocument.ExtensionAttribute;

public class ExtensionAttributeImpl implements ExtensionAttribute {
  private String namespace;
  private String prefix;
  private String name;
  private String text;

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getText() {
    return text;
  }

  public ExtensionAttributeImpl setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  public ExtensionAttributeImpl setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  public ExtensionAttributeImpl setName(final String name) {
    this.name = name;
    return this;
  }

  public ExtensionAttributeImpl setText(final String text) {
    this.text = text;
    return this;
  }

}
