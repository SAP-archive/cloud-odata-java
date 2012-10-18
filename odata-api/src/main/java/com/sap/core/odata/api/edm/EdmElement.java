package com.sap.core.odata.api.edm;

public interface EdmElement extends EdmMappable, EdmNamed, EdmTyped {

  EdmFacets getFacets() throws EdmException;
}
