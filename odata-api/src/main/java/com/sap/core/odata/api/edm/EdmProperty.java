package com.sap.core.odata.api.edm;


public interface EdmProperty extends EdmElement, EdmMappable, EdmNamed, EdmTyped {

  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;;

  String getMimeType() throws EdmException;;
}
