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
package com.sap.core.odata.core.edm.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNamed;

public abstract class EdmNamedImplProv implements EdmNamed {

  private static final Pattern PATTERN_VALID_NAME = Pattern
      .compile("^[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02ff\\u0370-\\u037d"
          + "\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f\\u2c00-\\u2fef\\u3001-\\ud7ff"
          + "\\uf900-\\ufdcf\\ufdf0-\\ufffd\\x10000-\\xEFFFF]"
          + "[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6"
          + "\\u00F8-\\u02ff\\u0370-\\u037d\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f"
          + "\\u2c00-\\u2fef\\u3001-\\udfff\\uf900-\\ufdcf\\ufdf0-\\ufffd\\-\\.0-9"
          + "\\u00b7\\u0300-\\u036f\\u203f-\\u2040]*\\Z");
  protected EdmImplProv edm;
  private String name;

  public EdmNamedImplProv(final EdmImplProv edm, final String name) throws EdmException {
    this.edm = edm;
    this.name = getValidatedName(name);
  }

  @Override
  public String getName() throws EdmException {
    return name;
  }

  private String getValidatedName(final String name) throws EdmException {
    Matcher matcher = PATTERN_VALID_NAME.matcher(name);
    if (matcher.matches()) {
      return name;
    }
    throw new EdmException(EdmException.COMMON);
  }
}
