package com.sap.core.odata.core.edm;

public interface EdmElement extends EdmMappable, EdmNamed, EdmTyped {

  EdmFacets getFacets() throws EdmException;
}
