package com.sap.core.odata.core.edm.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNamed;

public abstract class EdmNamedImplProv implements EdmNamed {

  private static final Pattern pattern = Pattern
      .compile("^[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02ff\\u0370-\\u037d"
          + "\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f\\u2c00-\\u2fef\\u3001-\\ud7ff"
          + "\\uf900-\\ufdcf\\ufdf0-\\ufffd\\x10000-\\xEFFFF]"
          + "[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6"
          + "\\u00F8-\\u02ff\\u0370-\\u037d\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f"
          + "\\u2c00-\\u2fef\\u3001-\\udfff\\uf900-\\ufdcf\\ufdf0-\\ufffd\\-\\.0-9"
          + "\\u00b7\\u0300-\\u036f\\u203f-\\u2040]*\\Z");
  protected EdmImplProv edm;
  private String name;

  public EdmNamedImplProv(EdmImplProv edm, String name) throws EdmException {
    this.edm = edm;
    this.name = name;
    validateName(name);
  }

  @Override
  public String getName() throws EdmException {
    return name;
  }

  private void validateName(String name) throws EdmException {
    Matcher matcher = pattern.matcher(name);
    if (!matcher.matches()) {
      throw new EdmException(EdmException.COMMON);
    }
  }
}
