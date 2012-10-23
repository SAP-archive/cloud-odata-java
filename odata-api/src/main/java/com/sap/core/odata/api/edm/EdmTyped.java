package com.sap.core.odata.api.edm;

public interface EdmTyped extends EdmNamed {

  EdmType getType() throws EdmException;;

  EdmMultiplicity getMultiplicity() throws EdmException;;
}
