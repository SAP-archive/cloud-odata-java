package com.sap.core.odata.core.edm;

public interface EdmTyped extends EdmNamed {

  EdmType getType() throws EdmException;;

  EdmMultiplicity getMultiplicity() throws EdmException;;
}
